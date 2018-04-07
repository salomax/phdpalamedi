// Tasks
(function($){

    $.fn.renderTasks = function(data) {
        console.log('Rendering tasks...');

        this.empty();

        var $row = $("<div class='row'>");
        this.append($row);

        $row.toggleClass('task-active', data.activeCount > 0);

        var $col = $("<div class='col-sm-4 total'>");
        $col.append($("<small>Max Pool Size </small>"));
        $col.append(document.createTextNode(data.maxPoolSize));
        $col.appendTo($row);

        var $col = $("<div class='col-sm-4 total'>");
        $col.append($("<small>Active Threads </small>"));
        $col.append(document.createTextNode(data.activeCount));
        $col.appendTo($row);

        var $col = $("<div class='col-sm-4 total'>");
        $col.append($("<small>Queue Size </small>"));
        $col.append(document.createTextNode(data.queueSize));
        $col.appendTo($row);

        return this;
    };

    $.fn.getLiveTasks = function() {
        var self = this;
        console.log('Loading tasks...');
        $.ajax({
            type: 'GET',
            url: "/task/live",
            dataType: 'json',
            success: function(result){
                console.log('Tasks loaded successfully');
                console.log(result);
                self.renderTasks(result);
            }
        });

        return this;
    };


})(jQuery);



