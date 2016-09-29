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

        $("#userInput").select2({
            placeholder: "Type word",
            minimumInputLength: 1,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "./play/words",
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
                    console.log(data);
                    for(var i = 0; i < data.length; i++) {
                        words.push({
                            id: data[i],
                            text: data[i]
                        });
                    }
                    console.log(words);
                    return { results: words };
                }
            }
        });
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
