var kyubot = new Kyubot();

// Start the conversation as soon as the document is ready
$(document).ready(function () {

    // Add handler for submitting messages to the KyuBOT...
    $("#kyubotQuery").submit(function (event) {
        kyubot.submitQueryToWatson($(this), event);
    });

    // ... and begin the conversation
    kyubot.initiateConversation();
});
