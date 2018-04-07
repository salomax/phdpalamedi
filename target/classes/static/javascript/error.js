// Tasks
(function($){

    $.fn.renderErrors = function(data) {
        var self = this;

        console.log('Rendering errors...');

        this.empty();

        var $total = $("<div class='card total'>");
        self.append($total);
        $total.append(document.createTextNode("Total de registros " + data.length));

        data.forEach(function(error) {

            var $card = $("<div class='card error'>");
            self.append($card);

            var $row = $("<div class='row'>");
            $row.appendTo($card);

            var $publisher = $("<div class='col-sm-12 publisher'>");
            $publisher.appendTo($row);
            $publisher.append(document.createTextNode(error.publisherName));

            var $message = $("<div class='col-sm-12'>");
            $message.appendTo($row);

            var $pre = $("<p>");
            $pre.appendTo($message);
            $pre.append(document.createTextNode(error.message));

            if (error.exception) {

                var $exception = $("<div class='exception'>");
                $exception.appendTo($row);
                $exception.append(document.createTextNode(error.exception));

            }

        });

        return this;
    };

    $.fn.loadErrors = function() {
        var self = this;
        console.log('Loading errors...');

        var publisherId = getUrlParameter('publisher_id');

        $.ajax({
            type: 'GET',
            url: "/error" + ( publisherId ? "?publisher_id=" + publisherId : ""),
            dataType: 'json',
            success: function(result){
                console.log('Errors loaded successfully');
                console.log(result);
                self.renderErrors(result);
            }
        });

        return this;
    };

    getUrlParameter = function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : sParameterName[1];
            }
        }
    };


})(jQuery);



