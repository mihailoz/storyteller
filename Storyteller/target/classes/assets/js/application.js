$(function() {
    var playerId, gameActive = true;
    
    var checkStatus = function () {
        $.ajax({
            url: "./play/status/" + playerId,
            dataType: "text",
            success: function (data) {
                var response = JSON.parse(data);
                
                if(response.status.string === "queueing") {
                    setTimeout(function () {
                        checkStatus();
                    }, 2000);
                } else if(response.status.string === "inGame") {
                    if(response.onTurn.string === "true") {
                        $("#submit").prop("disabled", false);
                        console.log("Your turn :D");
                        $("#storyParagraph").text(response.story.string);
                        setTimeout(function () {
                            checkStatus();
                            $("#submit").prop("disabled", true);
                        }, 8000);
                    } else {
                        $("#submit").prop("disabled", true);
                        $("#storyParagraph").text(response.story.string);
                        setTimeout(function () {
                            checkStatus();
                        }, 2000);
                    }
                    
                }
            }
        });
    }
    
    $('#submit').on('click', function (e) {
        var word = $("#nekiInput").val();
        $.ajax({
            type: "POST",
            url: "./play/turn/" + playerId + "/" + word,
            data: {},
            success: function (data) {
                $("#submit").prop("disabled", true);
                checkStatus();
            }
        });
    });
    
    var startGame = function() {
        checkStatus();
    };
    
    $("#findGame").click(function() {
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
    
});
