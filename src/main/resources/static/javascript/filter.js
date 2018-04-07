// Search
(function($){

    $.fn.renderFilter = function() {
        console.log('Rendering filter...');
        var self = this;

        this.empty();

        var $row = $("<div class='row filter'>");
        this.append($row);

        var $input = $('<input class="search col-sm-9" placeholder="Adicionar Tags">');
        $input.appendTo($row);

        $.ajax({
            type: 'GET',
            url: "/tag/all",
            dataType: 'json',
            success: function(result) {
                console.log('Tags loaded successfully');
                console.log(result);

                var tagify = $input.tagify({ whitelist: result });

            }
        });

        var $buttonSearch = $('<button type="button" class="btn btn-success col-sm-3">Pesquisar</button>');
        $buttonSearch.appendTo($row);

        var $resultBox = $('<div class="result-box">');
        $resultBox.appendTo($row);

        $buttonSearch.bind('click', function() {
            self.filter($input.val(), $resultBox);
        });

        return this;
    };

    $.fn.filter = function(tags, $resultBox) {
        var self = this;
        console.log('Filtering by tags', tags);

        $resultBox.empty();
        $resultBox.append($('<span>Carregando...</span>'));

        $.ajax({
            type: 'GET',
            url: "/article/filter?tags=" + tags,
            dataType: 'json',
            success: function(result) {
                console.log('Article loaded successfully');
                console.log(result);
                self.renderSearchResult(result, $resultBox);
            }
        });

        return this;
    };


})(jQuery);



