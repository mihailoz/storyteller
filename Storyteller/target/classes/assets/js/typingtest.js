$(function () {
    var startTime,
        endTime;

    var startGame = function () {
        var selected = 0,
            selectedSentences = [],
            indexes = [];

        while(selected !== 3) {
            var rand = Math.floor(Math.random() * 10) % sentences.length;

            for(var x = 0; x < selected; x++) {
                if(rand === indexes[x]) {
                    rand = Math.floor(Math.random() * 10) % sentences.length;
                    x = 0;
                }
            }

            selectedSentences.push(sentences[rand]);
            indexes.push(rand);
            selected++;
        }
        var fullText = selectedSentences.join('. '),
            wordByWord = fullText.split(' '),
            doneText = "",
            currentWord = 0;


        $('#unfinishedText').text(fullText);
        $('#finishedText').text(doneText);
        $('#userInput').val('').removeClass('incorrect');
        $('#userInput').focus();
        startTime = new Date();

        $('#userInput').keyup(function (e) {
            var value = $(e.target).val();
            if(value === (wordByWord[currentWord] + " ") || (value === wordByWord[currentWord] && currentWord === (wordByWord.length - 1))) {
                doneText = doneText + value;
                fullText = fullText.substring(value.length, fullText.length);

                $('#unfinishedText').text(fullText);
                $('#finishedText').text(doneText);

                currentWord++;

                $('#userInput').val('').removeClass('incorrect');
            } else {
                if(currentWord < wordByWord.length && value !== wordByWord[currentWord].substring(0, value.length)) {
                    $('#userInput').addClass('incorrect');
                } else {
                    $('#userInput').removeClass('incorrect');
                }
            }

            if(currentWord === wordByWord.length) {
                endTime = new Date();
                var completionTime = endTime - startTime;
                $('#finish-time').text((completionTime / 1000).toString() + " seconds");
                $('#completionModal').modal('show');
                $('#userInput').off('keyup');
            }
        });
    };

    $('#startButton').click(function () {
        startGame();
    });


});
