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

    addMessage("user", $("#inputText").val());
    // Clear input
    $('#inputText').val([]);
    addTypingIndicator();

    $.ajax({
        type: "POST",
        url: url,
        data: formData,
        success: function (data) {
            updateConversation(data);
        }
    });
    e.preventDefault(); // avoid to execute the actual submit of the form.
});

function updateConversation(data) {
    // Store the Watson context
    $('#watson_context').val(JSON.stringify(data.context));
    removeTypingIndicator();
    addMessage("kyubot", data.output.text[0])
    //$('#result').html("-- " + data.output.text[0]);
}

function addMessage(type, message) {
    $( ".messages" ).append("<div class='message'><div class='" + type + "'>" + message + "</div></div>");
    scrollToBottom();
}

function addTypingIndicator() {
    $( ".messages" ).append( "<div class='typing-indicator-container'><div class='typing-indicator'><span></span><span></span><span></span></div></div>");
    scrollToBottom();
}

function scrollToBottom() {
    scrollDiv = $(".messages");
    scrollDiv.animate({ scrollTop: scrollDiv.prop("scrollHeight")}, 1000);
    //$(".messages").animate({ scrollTop: $('.messages').prop("scrollHeight")}, 1000);
}

function removeTypingIndicator() {
    $( "div" ).remove( ".typing-indicator-container" );
}
