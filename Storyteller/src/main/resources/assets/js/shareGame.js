$(function () {
    var gameId = window.location.search;
    gameId = gameId.substring(1, gameId.length);
    $.ajax({
        url: "./play/history/" + gameId,
        dataType: "text",
        success: function(data) {
            $('#storyParagraph').text(data);
        }
    })
});
