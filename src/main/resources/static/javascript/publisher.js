// Load Publishers
(function($){


    $.fn.renderPublisherDetails = function(id, data) {
        var self = this;

        var $row = $("<div class='row publisher-details'>");
        this.append($row);

        var $col = $("<div class='col-sm-3 total'>");
        $col.append(document.createTextNode(data.totalArticles));
        $col.append($("<small> arquivos carregados</small>"));
        $col.appendTo($row);
        var $col = $("<div class='col-sm-3 success'>");
        $col.append(document.createTextNode(data.totalArticlesSuccess));
        $col.append($("<small> com sucesso</small>"));
        $col.appendTo($row);
        var $col = $("<div class='col-sm-3 error'>");
        $col.append(document.createTextNode(data.totalArticlesError));
        $col.append($("<small> com erro</small>"));
        $col.appendTo($row);

        var $col = $("<div class='col-sm-3 total'>");
        $col.appendTo($row);

        var $refresh = $('<button class="btn btn-default float-sm-right">Atualizar</button>');
        $refresh.bind('click', function() {
            $row.remove();
            self.loadPublisherDetails(id);
        });
        $refresh.appendTo($col);


    };

    $.fn.loadPublisherDetails = function(id) {
        var self = this;

        $.ajax({
            type: 'GET',
            url: "/publisher/" + id + "/details",
            dataType: 'json',
            success: function(result){
                console.log('Publisher Details loaded successfully');
                self.renderPublisherDetails(id, result);
            }
        });

    };

    $.fn.renderPublishers = function(data) {
        console.log('Rendering publishers...');

        var self = this;
        this.empty();

        data.forEach(function (item) {

            var $card = $("<div class='card'>");
            $card.appendTo(self);

            var $header = $('<div class="header">');
            $header.appendTo($card);

            var $tdName = $('<h5>');
            $tdName.append(document.createTextNode(item.name));
            $tdName.appendTo($header);

            $header.loadPublisherDetails(item.id);

            var $cardBody = $("<div class='card-body'>");
            $cardBody.appendTo($card);

            var $row = $("<div class='row'>");
            $row.appendTo($cardBody);

            var $colUrl = $("<div class='col-sm-6'>");
            $colUrl.append(document.createTextNode( "Pesquisar em" ));
            $colUrl.appendTo($row);

            if (item.url) {
                item.url.forEach(function(url) {
                    var $a = $('<a>');
                    $a.attr('target', '_blank');
                    $a.attr('href', url.url);
                    $a.html(url.url);
                    $('<div>').append($a).appendTo($colUrl);
                });
            }

            var $colFilter = $("<div class='col-sm-6'>");
            $colFilter.append(document.createTextNode( "Filtros" ));
            $colFilter.appendTo($row);
            if (item.filters) {
                item.filters.forEach(function(filter) {
                    var $a = $('<a>');
                    $a.attr('target', '_blank');
                    $a.attr('href', filter.url);
                    $a.html(filter.url);
                    $('<div>').append($a).appendTo($colFilter);
                });
            }

            var $button = $('<button>');
            $button.addClass('btn btn-success btn-load');
            $button.html('Carregar Artigos');
            $button.bind('click', function() {
                console.log('Load publisher ' + item.name)
                $.ajax({
                    type: 'POST',
                    url: "/publisher/" + item.id,
                    dataType: 'json',
                    success: function(){
                        console.log('Executor task started successfully');
                    }
                });
            });
            $button.appendTo($cardBody);

        });

        return this;
    };

    $.fn.loadPublishers = function() {
        var self = this;
        console.log('Loading publishers...')
        $.ajax({
            type: 'GET',
            url: "/publisher",
            dataType: 'json',
            success: function(result){
                console.log('Publishers loaded successfully');
                self.renderPublishers(result);
            }
        });

        return this;
    };


})(jQuery);



