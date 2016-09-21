$(function() {
    var playerId, gameActive = true;
    
    var checkStatus = function () {
        $.ajax({
            url: "./play/status/" + playerId,
            dataType: "text",
            success: function (data) {
                var response = JSON.parse(data);
                
                if(response.status.string === "queueing") {
                    $("#storyParagraph").text("Queueing, please wait...");
                    setTimeout(function () {
                        checkStatus();
                    }, 2000);
                } else if(response.status.string === "inGame") {
                    if(response.onTurn.string === "true") {
                        $("#submitButton").prop("disabled", false);
                        console.log("Your turn :D");
                        $("#storyParagraph").text(response.story.string);
                        setTimeout(function () {
                            checkStatus();
                            $("#submitButton").prop("disabled", true);
                        }, 8000);
                    } else {
                        $("#submitButton").prop("disabled", true);
                        $("#storyParagraph").text(response.story.string);
                        setTimeout(function () {
                            checkStatus();
                        }, 2000);
                    }
                    
                }
            }
        });
    }
    
    $('#submitButton').on('click', function (e) {
        var word = $("#userInput").val();
        $.ajax({
            type: "POST",
            url: "./play/turn/" + playerId + "/" + word,
            data: {},
            success: function (data) {
                $("#submitButton").prop("disabled", true);
                checkStatus();
            }
        });
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

    window.onbeforeunload = function () {
       $.ajax({
           type: "POST",
           url: "./play/leave/" + playerId,
           data: {}
       }); 
    };
    
});
