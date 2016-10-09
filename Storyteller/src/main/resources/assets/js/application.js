$(function() {
    var playerId, gameId, gameActive = true,
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
            url: "./api/play/status/" + playerId,
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

                    if(gameId === undefined) {
                        gameId = response.gameId.string;
                    }

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
                    window.location.replace("./shareGame.html?" + gameId);
                }
            }
        });
    }
    
    var submitFunc = function (word) {
        if(word === ".") {
            word = "endSentenceDot";
        }
        if(word ==="?") {
            word = "endSentenceQuestionMark";
        }
        if(word === "!") {
            word = "endSentenceExclamationMark";
        }

        $.ajax({
            type: "POST",
            url: "./api/play/turn/" + playerId + "/" + word,
            data: {},
            success: function(data) {
                $(".tajmer").hide();
                $("#submitButton").prop("disabled", true);
                $("#pollButton").prop("disabled", true);
                $("#userInput").val("");
            }
        });
    };

    $('#submitButton').on('click', function () {
        var word = $("#userInput").val();
        submitFunc(word);
    });

    $("#pollButton").on('click', function() {
        $.ajax({
            type: "POST",
            url: "./api/play/turn/" + playerId + "/endOfGamePoll",
            data: {},
            success: function(data) {
                $('#myModal').modal("show");
            }
        });
    });

    $("#pollYes").on('click', function() {
        $.ajax({
            type: "POST",
            url: "./api/play/poll/" + playerId + "/endGame",
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
            url: "./api/play/poll/" + playerId + "/dontEndGame",
            data: {},
            success: function(data) {
                pollFinished = true;
            }
        });
    });

    // Game logistics

    playerId = window.location.search;
    playerId = playerId.substring(1, playerId.length);

    checkStatus();
    $("#submitButton").prop("disabled", true);
    $("#pollButton").prop("disabled", true);

    $("#userInput").select2({
        placeholder: "Click to type",
        minimumInputLength: 1,
        ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
            url: "./api/play/words",
            dataType: 'json',
            quietMillis: 250,
            data: function(term, page) {
                return {
                    q: term, // search term
                };
            },
            results: function(data, page) { // parse the results into the format expected by Select2.
                // since we are using custom formatting functions we do not need to alter the remote JSON data
                var words = [];
                for(var i = 0; i < data.length; i++) {
                    words.push({
                        id: data[i],
                        text: data[i]
                    });
                }
                return { results: words };
            }
        }
    }).on("select2-selecting", function (e) {
        submitFunc(e.val);
    });

    window.onbeforeunload = function() {
        $.ajax({
            type: "POST",
            url: "./api/play/leave/" + playerId,
            data: {}
        });
    };

});
