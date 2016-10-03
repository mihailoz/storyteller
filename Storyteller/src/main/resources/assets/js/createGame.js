$(function () {
    $.ajax({
        type: "GET",
        url: "./api/game/createGame",
        data: {
            gameName: "new game",
            playerName: "miki",
            gamePassword: "something"
        },
        success: function(data) {
            console.log(data);
        }
    });
});
