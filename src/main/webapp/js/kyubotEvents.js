var kyubot = new Kyubot();
// Start the conversation as soon as the document is ready
$(document).ready(function () {
    kyubot.initiateConversation();
});

// Submit messages to the KyuBOT
$("#kyubotQuery").submit(function (e) {
    kyubot.submitQueryToKyubot($(this), e);
});