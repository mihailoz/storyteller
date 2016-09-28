$(function() {
    var playerId, gameActive = true,
        pollFinished = false,
        tajmer = false;

    var timerFunc = function(br) {
        $("#tajmer").text(br);
        if (br > 0) {
            setTimeout(function() {
                timerFunc(br - 1);
            }, 1000);
        }
    }

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
                        pollFinished = false;


                        if (tajmer === false) {
                            $(".tajmer").show();
                            timerFunc(8);
                        }
                        tajmer = true;

                        $("#submitButton").prop("disabled", false);
                        $("#pollButton").prop("disabled", false);
                        $("#userInput").prop("disabled", false);
                        $("#userInput").focus();

                        $("#storyParagraph").text(response.story.string);

                        setTimeout(function() {
                            checkStatus();
                        }, 700);
                    } else if (response.onTurn.string === "false") {
                        pollFinished = false;
                        $(".tajmer").hide();
                        $("#submitButton").prop("disabled", true);
                        $("#pollButton").prop("disabled", true);
                        $("#userInput").prop("disabled", true);
                        $("#userInput").val("");
                        $("#storyParagraph").text(response.story.string);
                        tajmer = false;
                        setTimeout(function() {
                            checkStatus();
                        }, 700);
                    } else if (response.onTurn.string === "endGamePoll") {
                        $(".tajmer").hide();
                        if (!($('#myModal').hasClass('in')) && !pollFinished) {
                            $('#myModal').modal("show");
                        }
                        tajmer = false;
                        setTimeout(function() {
                            checkStatus();
                        }, 700);
                    }

                } else if (response.status.string === "gameFinished") {
                    $('#pollButton').hide();
                    $(".tajmer").show();
                    $("#tajmer").text("The game has ended");
                }
            }
        });
    }

    $('#submitButton').on('click', function(e) {
        var word = $("#userInput").val();
        $.ajax({
            type: "POST",
            url: "./play/turn/" + playerId + "/" + word,
            data: {},
            success: function(data) {
                $(".tajmer").hide();
                $("#submitButton").prop("disabled", true);
                $("#pollButton").prop("disabled", true);
                $("#userInput").val("");
            }
        });
    });

    $("#pollButton").on('click', function() {
        $.ajax({
            type: "POST",
            url: "./play/turn/" + playerId + "/endOfGamePoll",
            data: {},
            success: function(data) {
                $('#myModal').modal("show");
            }
        });
    });

    $("#pollYes").on('click', function() {
        $.ajax({
            type: "POST",
            url: "./play/poll/" + playerId + "/endGame",
            data: {},
            success: function(data) {
                pollFinished = true;
                $('#myModal').modal("hide");
            }
        });
    });

    $("#pollNo").on('click', function() {
        $('#myModal').modal("hide");
        $.ajax({
            type: "POST",
            url: "./play/poll/" + playerId + "/dontEndGame",
            data: {},
            success: function(data) {
                pollFinished = true;
            }
        });
    });

    // Game logistics
    var startGame = function() {
        checkStatus();
        $("#submitButton").prop("disabled", true);
        $("#pollButton").prop("disabled", true);
    };

    $("#FbShare").on('click', function () {
        var fbpopup = window.open("https://www.facebook.com/sharer/sharer.php?u=file:///home/mihailo/ManHut/projects/storyteller/Storyteller/src/main/resources/assets/inGame.html", "pop", "width=600, height=400, scrollbars=no");
        return false;
    });

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
