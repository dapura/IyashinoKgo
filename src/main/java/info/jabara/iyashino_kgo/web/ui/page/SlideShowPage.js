"use strict";

jQuery(function($) {

    var deactivateApplicationIfNotSupportFileApi = function() {
        if (window.FileReader) return false;
        var droppableArea = $('.droppable-area');
        droppableArea.find('> *').each(function(i, e) {
            var elem = $(e);
            if (elem.hasClass('logo')) {
                return;
            }
            elem.hide();
        });
        droppableArea.find('> .sorry').show();
        return true;
    };

    if (deactivateApplicationIfNotSupportFileApi()) {
        return;
    }

    var maxFileCount = 9;
    var maxFileSize = 1024 * 1024 * 5; // 5MB
    var _fileCount = 0;
    var _files = {};

    var activateIndicator = (function() {
        var ind = $('#indicator-container');
        return function() {
            ind.show();
            ind.activity();
        };
    })();

    var deactivateIndicator = (function() {
        var ind = $('#indicator-container');
        return function() {
            ind.hide();
            ind.activity(false);
        };
    })();

    var cancel = function(e) {
        e.preventDefault();
        e.stopPropagation();
        return false;
    };

    var counter = (function() {
        var count = 0;
        return function() {
            return ++count;
        };
    })();

    var initializeInputFile = function() {
        $('input[type="file"]').change(function(e) {
            processFiles(e.target.files);
        });
    };

    var initializeDroppableArea = function() {
        var droppable = $('div.droppable-area');
        droppable.on('dragenter', cancel);
        droppable.on('dragover', cancel);
        droppable.on('drop', function(e) {
            cancel(e);

            e.originalEvent.dataTransfer.dropEffect = 'copy';
            processFiles(e.originalEvent.dataTransfer.files);
        });
    };

    var processFiles = function(pFiles) {
        activateIndicator();
        for (var i = 0, len = pFiles.length; i < len; ++i) {
            processFile(pFiles[i]);
        }
    };

    var initializeImageBoxes = function() {
        // 画像の並べ替えと削除.
        // 複数存在することがあるので.imagesに対してイベントリスナを設定する.
        // これが出来るのがブラウザアプリの便利なところ.
        var images = $('.images');
        images.on('mouseover', '> .image-box', function(e) {
            $(this).find('> .control-box').stop().fadeIn('fast');
        });
        images.on('mouseout', '> .image-box', function(e) {
            // 画像の上からコントロールボックスにマウスカーソルが移動したときに当イベントが着火するが、このときはコントロールボックスを隠したくない.
            // そこでコントロールボックス内のコントロールには特定のclass値を付与するようにし、これでマウスカーソルのある位置を判定するようにする.
            var relatedTarget = $(e.relatedTarget);
            if (relatedTarget.hasClass('control-box')) return;
            if (relatedTarget.hasClass('control-box-control')) return;
            $(this).find('> .control-box').stop().fadeOut('fast');
        });
        images.on('click', '.control-box > button.upper', function() {
            var imageBox = $(this).parent().parent();
            imageBox.insertBefore(imageBox.prev());
            imageBox.find('> .control-box').hide();
        });
        images.on('click', '.control-box > button.downer', function() {
            var imageBox = $(this).parent().parent();
            imageBox.insertAfter(imageBox.next());
            imageBox.find('> .control-box').hide();
        });
        images.on('click', '.control-box > button.deleter', function() {
            var imageBox = $(this).parent().parent();
            imageBox.hide(function() {
                var imageId = imageBox.find('img').attr('id');
                imageBox.remove();
                delete _files[imageId];
                --_fileCount;
                if (_fileCount === 0) {
                    $('.droppable-area button.starter').hide();
                }
            });
        });
    };

    var initializeSlideShow = function() {
        $('button.starter').click(function() {
            if (_fileCount === 0) return;
            $('.image-container').fadeOut(function() {
                startMusic();
                startSlideShow();
                $('#control-box-in-slideshowing').find('> button.newer, > button.saver').fadeIn(1000);
            });
        });
    };

    var initializePersistentButtons = function() {
        var buttonContainer = $('#control-box-in-slideshowing');
        buttonContainer.find('> button.newer').click(function() {
            location.reload();
        });
        buttonContainer.find('> button.saver').click(function() {
            alert('will implement...');
        });
        buttonContainer.find('> button.selecter').click(function() {
            alert('will implement...');
        });
    };

    var suppressDefaultDropAndDropAction = function() {
        // 指定の場所以外にドロップしたときに画面遷移するのを防止
        $(document).on('dragenter', cancel);
        $(document).on('dragover', cancel);
        $(document).on('drop', cancel);
    };

    var _loadingCount = 0;
    var processFile = function(pFile) {
        if (_fileCount >= maxFileCount) return;
        if (pFile.type.lastIndexOf("image/", 0) != 0) return; // TODO エラー通知
        if (pFile.size >= maxFileSize) return; // TODO エラー通知

        var fr = new FileReader();
        $(fr).on('load', function(e) {
            var count = counter();
            var renderParam = {
                controlBoxId: 'controlBox_' + count,
                imageId: 'image_' + count,
                fileName: pFile.name
            };
            var html = $('#template').render(renderParam);
            $('.images').append(html);

            $('#' + renderParam.imageId).attr('src', e.target.result);
            $('#' + renderParam.controlBoxId).find('*').addClass('control-box-control');

            _files[renderParam.imageId] = pFile;
            ++_fileCount;
            $('.droppable-area button.starter').show();

            --_loadingCount;
            if (_loadingCount === 0) {
                deactivateIndicator();
            }
        });
        fr.readAsDataURL(pFile);
        ++_loadingCount;
        activateIndicator();
    }

    var startMusic = function() {
        var soundManager;
        SC.initialize({
            client_id: "c6ae25b0cafe3d32bce74d317ea49aa2",
            redirect_uri: "http://example.com/callback.html",
        });
        SC.stream("/tracks/147653855", {
            autoPlay: true,
            onfinish: function() {
                soundManager.play();
            }
        },
        function(pSoundManager) {
            soundManager = pSoundManager;
        });
    };

    var startSlideShow = function() {
        var maximage = $('#maximage');
        $('.images img').each(function(i, e) {
            maximage.append(e);
        });
        maximage.maximage({
            cycleOptions: {
                speed: 4000,
            timeout: 6850
            },
            onFirstImageLoaded: function() {
                maximage.fadeIn(2000);
            }
        });
    };

    initializeInputFile();
    initializeDroppableArea();
    initializeImageBoxes();
    initializeSlideShow();
    initializePersistentButtons();
    suppressDefaultDropAndDropAction();
});

