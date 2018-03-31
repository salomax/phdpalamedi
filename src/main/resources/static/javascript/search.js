// Search
(function($){

    $.fn.renderSearch = function() {
        console.log('Rendering search...');
        var self = this;

        this.empty();

        var $row = $("<div class='row search'>");
        this.append($row);

        var $inputSearch = $('<input type="text" class="search col-sm-9" />');
        $inputSearch.appendTo($row);

        var $buttonSearch = $('<button type="button" class="btn btn-success col-sm-3">Pesquisar</button>');
        $buttonSearch.appendTo($row);

        var $resultBox = $('<div class="result-box">');
        $resultBox.appendTo($row);

        $buttonSearch.bind('click', function() {
           self.search($inputSearch.val(), $resultBox);
        });

        return this;
    };

    $.fn.renderSearchResult = function(result, $resultBox) {
        var self = this;
        console.log('Rendering result');

        $resultBox.empty();
        $resultBox.append($('<div class="query"><pre>' + result.query + '</pre></div>'));
        $resultBox.append($('<span class="count">Artigos encontrados: ' + result.articles.length + ' </span>'));

        result.articles.forEach(function(item) {

            var $resultItem = $('<div class="result-item">');
            $resultItem.appendTo($resultBox);

            $resultItem.toggleClass('selected', item.selected);


            var $select = $('<button class="btn float-sm-right">' + (!item.selected ? 'Selecionar' : 'Remover Seleção') + '</button>');
            $select.appendTo($resultItem);
            $select.attr('data-selected', item.selected);
            $select.toggleClass('btn-primary', !item.selected);
            $select.bind('click', function() {
                var $selectThis = $(this);

                var selected = !($selectThis.attr('data-selected') == 'true');
                $.ajax({
                    type: 'PUT',
                    url: "/article/" + item.id + "/selected/" + selected,
                    success: function() {
                        console.log('Article updated successfully');
                        $selectThis.attr('data-selected', selected);
                        $select.html(!selected ? 'Selecionar' : 'Remover Seleção');
                        $select.toggleClass('btn-primary', !selected);
                        $resultItem.toggleClass('selected', selected);
                    }
                });

            });

            var $publisherName = $('<div><span>' + item.publisherName + '</span></div>');
            $publisherName.appendTo($resultItem);

            var $title = $('<div><a target="_blank" href="' + item.url + '">' + item.url + '</a></div>');
            $title.appendTo($resultItem);

            var $viewButtons = $('<div class="btn-group btn-group-views" role="group">');
            $viewButtons.appendTo($resultItem);

            var $viewContent = $('<button class="btn btn-secondary">Ver Texto</button>');
            $viewContent.appendTo($viewButtons);

            var $content = $('<div class="article-content">');
            $content.appendTo($resultItem);
            $content.hide();
            $viewContent.unbind();

            var openContent = function() {

                $.ajax({
                    type: 'GET',
                    url: "/article/" + item.id,
                    dataType: 'json',
                    success: function(article){
                        console.log('Article loaded successfully');''
                        console.log(article);

                        var $pre = $('<pre class="article-content">');
                        $pre.appendTo($content);
                        $pre.html(article.content);
                        $pre.mark(result.terms, { diacritics: true });

                        $content.show();

                        $viewContent.html('Fechar Texto');
                        $viewContent.unbind();
                        $viewContent.bind('click', function() {
                            $content.hide();
                            $content.html('');
                            $viewContent.html('Ver Texto');
                            $viewContent.unbind();
                            $viewContent.bind('click', openContent);
                        });

                    }

                });

            };

            $viewContent.bind('click', openContent);

        });

    };

    $.fn.search = function(search, $resultBox) {
        var self = this;
        console.log('Searching by ' + search);

        $resultBox.empty();
        $resultBox.append($('<span>Carregando...</span>'));

        $.ajax({
            type: 'GET',
            url: "/article?search=" + search,
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



