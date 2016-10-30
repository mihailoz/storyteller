$(function () {
    var gameId = window.location.search;
    gameId = gameId.substring(1, gameId.length);
    $.ajax({
        url: "./api/history/" + gameId,
        dataType: "text",
        success: function(data) {
            $('#storyParagraph').text(data);
        }
    });
    
    $('.back-button').on('click', function () {
       window.location.replace("./index.html"); 
    });
});
