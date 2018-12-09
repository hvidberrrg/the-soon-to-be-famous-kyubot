var kyubot = new Kyubot();

// Start the conversation as soon as the document is ready
$(document).ready(function () {
    kyubot.initiateConversation();
});

// Submit messages to the KyuBOT
$("#kyubotQuery").submit(function (event) {
    kyubot.submitQueryToWatson($(this), event);
});