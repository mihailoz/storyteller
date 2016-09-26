$(function() {
    var playerId, gameActive = true;

    var checkStatus = function() {
        $.ajax({
            url: "./play/status/" + playerId,
            dataType: "text",
            success: function(data) {
                var response = JSON.parse(data);

                if (response.status.string === "queueing") {
                    // If in queue
                    $("#storyParagraph").text("Queueing, please wait...");

                    setTimeout(function() {
                        checkStatus();
                    }, 700);
                } else if (response.status.string === "inGame") {
                    // If in game

                    if (response.onTurn.string === "true") {
                        // If on turn
                        $("#submitButton").prop("disabled", false);
                        $("#userInput").prop("disabled", false);
                        $("#submitButton").prop("disabled", false);
                        $("#userInput").focus();
                        $("#storyParagraph").text(response.story.string);
                        setTimeout(function() {
                            checkStatus();
                        }, 700);
                    } else {
                        $("#submitButton").prop("disabled", true);
                        $("#pollButton").prop("disabled", true);
                        $("#userInput").prop("disabled", true);
                        $("#storyParagraph").text(response.story.string);
                        setTimeout(function() {
                            checkStatus();
                        }, 700);
                    }

                }
            }
        });
    }

    $('#submitButton').on('click', function(e) {
        $("#userInput").val("");
        var word = $("#userInput").val();
        $.ajax({
            type: "POST",
            url: "./play/turn/" + playerId + "/" + word,
            data: {},
            success: function(data) {
                $("#submitButton").prop("disabled", true);
            }
        });
    });

    $("#pollButton").on('click', function() {
        $('#myModal').modal("show");
    });
    // Game logistics
    var startGame = function() {
        checkStatus();
        $("#submitButton").prop("disabled", true);
    };

    $.ajax({
        url: "./play",
        dataType: "text",
        success: function(data) {
            playerId = data;
            startGame();
        }
    });

    window.onbeforeunload = function() {
        $.ajax({
            type: "POST",
            url: "./play/leave/" + playerId,
            data: {}
        });
    };

});
