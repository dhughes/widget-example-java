var currentPage = 0;
var lastPage = 0;

$(function () {
    var page = 1;

    // get the default list of widgets
    listWidgets(currentPage, "", "", "");

    $("#search").click(function(){
        listWidgets(0);
        return false; // this prevents the form from being submitted when the button is clicked.
    });

    $("#reset").click(function(){
        $("#name").val("");
        $("#typeId").val("");
        $("#id").val("");

        listWidgets(0);
        return false; // this prevents the form from being submitted when the button is clicked.
    });


    $("#back").click(function(){
        if(currentPage > 0){
            listWidgets(currentPage-1);
        }
        return false;
    });

    $("#next").click(function(){
        if(currentPage < lastPage){
            listWidgets(currentPage+1);
        }
        return false;
    });

});

function listWidgets(page){
    var widgetName = $("#name").val();
    var widgetTypeId = $("#typeId").val();
    var widgetId = $("#id").val();

    currentPage = page;
    $.get(
        "/widgets?page=" + page + "&name=" + widgetName + "&typeId=" + widgetTypeId + "&id=" + widgetId,
        function(data) {
            // setup the paginator display
            var description = $("#description");
            var start = data.number * data.size  + 1;
            var end = start + data.size;
            var total = data.totalElements;

            lastPage = Math.ceil(total/data.size) - 1;

            description.text(start + " - " + end + " of " + total);

            var widgets = $("#widgets");
            widgets.empty();

            // check to see if the user is logged in
            // note: this isn't really very well done, but it's sufficient for our demonstration
            var loggedIn = $("#logout").length == 1;

            // iterate over the widgets from the ajax response
            data.content.forEach(function( widget ){
                var template = $("#template").clone();
                template.removeAttr("id");

                // set the image
                var img = template.find("img");
                img.attr("src", "/widget/image?id=" + widget.id);

                // set the name
                var widgetName = template.find(".widgetName");
                if(loggedIn){
                    // user is logged in, make the widget name a link
                    widgetName.append("<a href='/editWidget?id=" + widget.id + "'>" + widget.name + "</a>")
                } else {
                    // user is not logged in, just make the widget name be text
                    widgetName.append(widget.name);
                }

                // set the types
                var types = $(".types");
                types.empty();

                widget.types.forEach(function( type ){
                    types.append("<li>" + type.type + "</li>");
                });

                // set the width, height, etc
                template.find(".width").text(widget.width);
                template.find(".height").text(widget.height);
                template.find(".length").text(widget.length);
                template.find(".weight").text(widget.weight);

                // setup the notes
                var a = template.find(".notesLink a");
                a.attr("href", "/widgetNotes/?id=" + widget.id);

                a.text(widget.notes.length + " notes..");


                widgets.append(template);
            });

        }
    );
}