$(function () {
    var playerId = window.location.search;
    playerId = playerId.substring(1, playerId.length);

    var checkStatus = function() {
        $.ajax({
            url: "./api/play/status/" + playerId,
            dataType: "text",
            success: function(data) {

                if (data === "game-started") {
                    window.location.href = "inGame.html?" + playerId;
                } else {
                    var response = JSON.parse(data);
                    // If in game
                    console.log(response);
                }
            }
        });
    }
});
