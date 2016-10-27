$(function () {
    var playerId = window.location.search;
    playerId = playerId.substring(1, playerId.length);

    var checkStatus = function() {
        $.ajax({
            url: "./api/game/status/" + playerId,
            dataType: "text",
            success: function(data) {

                if (data === "game-started") {
                    window.location.href = "inGame.html?" + playerId;
                } else {
                    var response = JSON.parse(data);
                    // If in game
                    $("#playerNumberParagraph").text("number of players: " + response.playerNumber);
                    $("#gameName").text(" in game lobby: " + response.gameName);
                    if(!response.owner) {
                        $("#startGame").hide();
                    } else {
                        $("#startGame").show();
                    }
                    setTimeout(function() {
                        checkStatus();
                    }, 700);
                }
            }
        });
    };

  /*var submitFunc2 = function (word) {
        $.ajax({
            type: "POST",
            url: "./api/play/turn/" + playerId + "/" + word,
            data: {},
            success: function(data) {
                $("#typechat").val("");
            }
        });
    };
    $('#send').on('click', function () {
        var word = $("#typechat").val();
        submitFunc2(word);
    });
*/

    $('#startGame').on('click', function () {
        $.ajax({
            type: "POST",
            url: "./api/game/startGame/" + playerId,
            dataType: "text",
            success: function(data) {
                if (data === "Game started") {
                    window.location.href = "inGame.html?" + playerId;
                } else {
                    console.log("There has been an error");
                }
            }
        });
    });

    checkStatus();

    window.onbeforeunload = function() {
        $.ajax({
            type: "POST",
            url: "./api/game/leave/" + playerId,
            data: {}
        });
    };
});
