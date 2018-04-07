// Search
(function($){

    $.fn.renderSearch = function() {
        console.log('Rendering search...');
        var self = this;

        this.empty();
        
        var $row = $("<div class='row search'>");
        this.append($row);

        var $inputSearch = $('<textarea rows="4" class="search col-sm-9" />');
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

    $.fn.renderTags = function(item, tags) {

        var $input = $('<input placeholder="Adicionar Tags">');
        $input.appendTo(this);

        $input.val(item.tags)

        var tagify = $input.tagify({ whitelist: tags });

        tagify.on('add', function(e, tag) {

            var _data = {
                tag: tag.value,
                url: item.url
            };

            $.ajax({
                type: 'POST',
                url: "/tag",
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(_data),
                success: function () {
                    console.log('added', tag);
                }
            });

        });

        tagify.on('remove', function(e, tag) {

            var _data = {
                tag: tag.value,
                url: item.url
            };

            $.ajax({
                type: 'DELETE',
                url: "/tag",
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(_data),
                success: function () {
                    console.log('removed', tag);
                }
            });

        });

    }

    $.fn.renderSearchResult = function(result, $resultBox) {
        var self = this;
        console.log('Rendering result');

        $resultBox.empty();
        $resultBox.append($('<div class="query"><pre>' + result.query + '</pre></div>'));
        $resultBox.append($('<span class="count">Artigos encontrados: ' + result.articles.length + ' </span>'));

        result.articles.forEach(function(item) {

            var $resultItem = $('<div class="result-item">');
            $resultItem.appendTo($resultBox);

            var $row = $('<div><span>' + item.title + '</span></div>');
            $row.appendTo($resultItem);

            var $row = $('<div><a target="_blank" href="' + item.url + '">' + item.url + '</a></div>');
            $row.appendTo($resultItem);

            var $row = $('<div class="tag">');
            $row.appendTo($resultItem);

            $row.renderTags(item, result.tags);

            var $row = $('<div class="author"><span>' + item.author + '</span></div>');
            $row.appendTo($resultItem);

            var $row = $('<div class="abstract"><span>' + item.summary + '</span></div>');
            $row.mark(result.terms, { diacritics: true });
            $row.appendTo($resultItem);

            var $row = $('<div class="keywords"><span>' + item.keywords + '</span></div>');
            $row.mark(result.terms, { diacritics: true });
            $row.appendTo($resultItem);

            var $row = $('<div class="publisher"><span>' + item.publication.publisher + '</span></div>');
            $row.appendTo($resultItem);

            var $row = $('<div class="publication-version"><span>' + item.publication.name + '</span></div>');
            $row.appendTo($resultItem);

            if (item.publication.year) {
                var $row = $('<div class="publication-year"><span>' + item.publication.year + '</span></div>');
                $row.appendTo($resultItem);
            }

            var $row = $('<div class="publication-url"><a target="_blank" href="' + item.publication.url + '">' + item.publication.url + '</a></div>');
            $row.appendTo($resultItem);

            var $viewButtons = $('<div class="btn-group btn-group-views" role="group">');
            $viewButtons.appendTo($resultItem);

            if (item.articleContents) {

                var $row = $('<div class="download"><span>Arquivos</span></div>');
                $row.appendTo($resultItem);

                item.articleContents.forEach(function(download) {

                    var $row = $('<div class="publication-download"><a target="_blank" href="' + download.url + '">' + download.url + '</a></div>');
                    $row.appendTo($resultItem);

                    var $viewContent = $('<button class="btn btn-secondary">Ver Texto</button>');
                    $viewContent.appendTo($row);

                    var $content = $('<div class="article-content">');
                    $content.appendTo($resultItem);
                    $content.hide();
                    $viewContent.unbind();

                    var openContent = function() {

                        $.ajax({
                            type: 'GET',
                            url: "/articlecontent/" + download.id,
                            dataType: 'json',
                            success: function(article){

                                console.log('Article loaded successfully');
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

            }

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



