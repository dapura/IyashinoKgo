"use strict";

(function() {
	$(function() { initialize(); });

	var _counter = (function() {
		var count = 0;
		return function() {
			return count++;
		};
	})();
	
	var initialize = function() {
		var cancel = function(e) {
			e.preventDefault();
			e.stopPropagation();
			return false;
		};
		var droppable = $('div.droppable-area');
		droppable.on('dragenter', cancel);
		droppable.on('dragover', cancel);
		droppable.on('drop', function(e) {
			cancel(e);

			e.originalEvent.dataTransfer.dropEffect = 'copy';
			var files = e.originalEvent.dataTransfer.files;
			for (var i = 0, len = files.length; i < len; ++i) {
				processFile(files[i]);
			}
			return false;
		});

        // 画像の並べ替えと削除.
        // 複数存在することがあるので.imagesに対してイベントリスナを設定する.
        // これが出来るのがブラウザアプリの便利なところ.
        var images = $('.images');
        images.on('mouseover', '.image-box', function() {
            $(this).find('.control-box').show();
        });
        images.on('mouseout', '.image-box', function() {
            $(this).find('.control-box').hide();
        });
        images.on('click', '.control-box > button.upper', function() {
            var imageBox = $(this).parent().parent();
            imageBox.insertBefore(imageBox.prev());
        });
        images.on('click', '.control-box > button.downer', function() {
            var imageBox = $(this).parent().parent();
            imageBox.insertAfter(imageBox.next());
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

        // スライドショー開始
        $('button.starter').click(function() {
            if (_fileCount === 0) return;
            $('.image-container').fadeOut(function() {
                var maximage = $('#maximage');
                $('.images img').each(function(i, e) {
                    maximage.append(e);
                });
                maximage.maximage();
            });
        });

        // 指定の場所以外にドロップしたときに画面遷移するのを防止
        $(document).on('dragenter', cancel);
        $(document).on('dragover', cancel);
        $(document).on('drop', cancel);
    }

    var maxFileCount = 9;
    var maxFileSize = 1024 * 1024 * 5; // 5MB
    var _fileCount = 0;
    var _files = {};
    var processFile = function(pFile) {
        if (_fileCount >= maxFileCount) return;
        if (pFile.type.lastIndexOf("image/", 0) != 0) return; // TODO エラー通知
        if (pFile.size >= maxFileSize) return; // TODO エラー通知

        var fr = new FileReader();
        var images = $('.images');
        $(fr).on('load', function(e) {
            var imageId = 'image_' + _counter();
            var html = $('#template').render({ fileName: pFile.name, imageId: imageId });
            images.append(html);
            $('#' + imageId).attr('src', e.target.result);

            _files[imageId] = pFile;
            ++_fileCount;
            $('.droppable-area button.starter').show();
        });
        fr.readAsDataURL(pFile);
    }
})();

