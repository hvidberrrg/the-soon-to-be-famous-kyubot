// Define the KyuBOT class
var Kyubot = (function () {

    // Constructor
    function Kyubot() {
    }

    // Initialize the conversation with the KyuBOT
    Kyubot.prototype.initiateConversation = function () {
        // Indicate that the KyuBOT is preparing a reply
        this.addTypingIndicator();
        // Initiate the conversation with the KyuBOT
        var me = this;
        $.post("/conversation",
            function (data) {
                me.updateConversation(data);
            }
        );
    };

    // Submit messages to the KyuBOT
    Kyubot.prototype.submitQueryToKyubot = function (form, event) {
        var url = form.attr('action');
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
            var me = this;
            $.ajax({
                type: "POST",
                url: url,
                data: formData,
                success: function (data) {
                    me.updateConversation(data);
                }
            });
        }
        event.preventDefault(); // avoid to execute the actual submit of the form.
    };

    // Update the conversation with the reply received from the KyuBOT
    Kyubot.prototype.updateConversation = function (data) {
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

    return Kyubot;
}());