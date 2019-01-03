// Define the KyuBOT class
var Kyubot = (function () {
    var conversationUrl = "/conversation";

    // Constructor
    function Kyubot() {}

    // Initialize the conversation with the KyuBOT
    Kyubot.prototype.initiateConversation = function() {
        // Indicate that the KyuBOT is preparing a reply
        this.addTypingIndicator();
        // Initiate the conversation with the KyuBOT
        this.postToConversation("");
    };

    // Submit messages to the KyuBOT
    Kyubot.prototype.submitQueryToWatson = function(form, event) {
        // serializes the form's elements before clearing the input
        var formData = form.serialize();

        // Add the user's message to the conversation
        var inputText = $("#inputText");
        var message = inputText.val();
        if (message && (message = $.trim(message))) {
            this.addMessage("user", message);
            // Clear input
            inputText.val([]);
            // Indicate that the KyuBOT is preparing a reply
            this.addTypingIndicator();

            // Get the KyuBOT's reply and update the conversation
            this.postToConversation(formData);
        }
        event.preventDefault(); // avoid to execute the actual submit of the form.
    };

    // Post a new query to Watson and add the reply to the conversation
    Kyubot.prototype.postToConversation = function(userInput) {
        // define callback
        var me = this;
        var onConversationSuccess = function(data) {
            me.updateConversation(data);
        };
        // perform ajax post
        this.post(conversationUrl, userInput, onConversationSuccess);
    };

    // Perform ajax post
    Kyubot.prototype.post = function(url, data, onSuccess) {
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: onSuccess
        });
    };

    // Update the conversation with the reply received from the KyuBOT
    Kyubot.prototype.updateConversation = function(data) {
        // Store the Watson context
        $('#watson_context').val(JSON.stringify(data.context));
        // Remove the animation indicating that the KyuBOT is "typing"
        this.removeTypingIndicator();
        // Add the KyuBOT's reply to the conversation
        this.addMessage("kyubot", data.output.text[0])
    };

    // Add a new message to the conversation
    Kyubot.prototype.addMessage = function(type, message) {
        $(".messages").append("<div class='message'><div class='" + type + "'>" + message + "</div></div>");
        this.scrollToBottom();
    };

    // Indicate that the KyuBOT is preparing a reply
    Kyubot.prototype.addTypingIndicator = function() {
        $(".messages").append("<div class='typing-indicator-container'><div class='typing-indicator'><span></span><span></span><span></span></div></div>");
        this.scrollToBottom();
    };

    Kyubot.prototype.scrollToBottom = function() {
        var scrollDiv = $(".messages");
        scrollDiv.animate({scrollTop: scrollDiv.prop("scrollHeight")}, 1000);
    };

    Kyubot.prototype.removeTypingIndicator = function() {
        $("div").remove(".typing-indicator-container");
    };

    // Display/hide overlay
    Kyubot.prototype.displayOverlay = function(event, url) {
        event.preventDefault(); // avoid to visit the clicked link

        $("#overlay_content").load(url,
            // the following is the callback
            function(){$("#overlay").fadeIn(300).scrollTop(0)});
    };

    Kyubot.prototype.hideOverlay = function() {
        $("#overlay").fadeOut('slow');
    };



    return Kyubot;
}());