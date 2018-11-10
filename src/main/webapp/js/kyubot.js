// Start the conversation as soon as the document is ready
$(document).ready(function () {
    addTypingIndicator();
    $.post("/conversation",
        function (data) {
            updateConversation(data);
        }
    );
});

// Submit messages to the KyuBOT
$("#kyubotQuery").submit(function (e) {
    var form = $(this);
    var url = form.attr('action');
    // serializes the form's elements before clearing the input
    var formData = form.serialize();

    // Add the user's message to the conversation
    var inputText = $("#inputText");
    var message = inputText.val();
    if (message && (message = $.trim(message))) {
        addMessage("user", message);
        // Clear input
        inputText.val([]);
        // Indicate that the KyuBOT is preparing a reply
        addTypingIndicator();

        // Get the KyuBOT's reply and update the conversation
        $.ajax({
            type: "POST",
            url: url,
            data: formData,
            success: function (data) {
                updateConversation(data);
            }
        });
    }
    e.preventDefault(); // avoid to execute the actual submit of the form.
});

function updateConversation(data) {
    // Store the Watson context
    $('#watson_context').val(JSON.stringify(data.context));
    // Remove the animation indicating that the KyuBOT is "typing"
    removeTypingIndicator();
    // Add the KyuBOT's reply to the conversation
    addMessage("kyubot", data.output.text[0])
}

function addMessage(type, message) {
    $(".messages").append("<div class='message'><div class='" + type + "'>" + message + "</div></div>");
    scrollToBottom();
}

function addTypingIndicator() {
    $(".messages").append("<div class='typing-indicator-container'><div class='typing-indicator'><span></span><span></span><span></span></div></div>");
    scrollToBottom();
}

function scrollToBottom() {
    var scrollDiv = $(".messages");
    scrollDiv.animate({scrollTop: scrollDiv.prop("scrollHeight")}, 1000);
}

function removeTypingIndicator() {
    $("div").remove(".typing-indicator-container");
}