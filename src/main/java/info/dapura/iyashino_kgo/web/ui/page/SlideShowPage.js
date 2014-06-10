"use strict";

(function() {
	$(function() { initialize(); });

	var counter = (function() {
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
            if (confirm('削除します。よろしいですか？')) {
                var imageBox = $(this).parent().parent();
                var imageId = imageBox.find('img').attr('id');
                imageBox.remove();
                delete files_[imageId];
                console.log(files_);
            }
        });

        $('button.starter').click(function() {
            alert('Not Implemented.');
        });
	}

    var files_ = {};
	var processFile = function(pFile) {
		var fr = new FileReader();
        var images = $('.images');
		$(fr).on('load', function(e) {
			var imageId = 'image_' + counter();
            var html = $('#template').render({ imageId: imageId });
            images.append(html);
			$('#' + imageId).attr('src', e.target.result);

            files_[imageId] = pFile;
            console.log(files_);
		});
		fr.readAsDataURL(pFile);
	}
})();

