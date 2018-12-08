describe('KyuBOT Jasmine unit tests', function () {
    var kyubot;
    var scrollHeight;

    // Initialize the kyubot
    beforeAll(function () {
        kyubot = new Kyubot();
        scrollHeight = 10000;
    });

    // Add an element to render into
    beforeEach(function () {
        testElement = $('<div class="conversation">\n' +
            '        <form class="chat" action="/conversation" method="post" id="kyubotQuery">\n' +
            '            <div class="messages" style="height:' + scrollHeight + 'px;">&nbsp;</div>\n' +
            '            <input type="text" id="inputText" name="input" placeholder="Enter your message here...">\n' +
            '            <input type="hidden" id="watson_context" name="context" value="">\n' +
            '            <input type="submit" value="Send">\n' +
            '        </form>\n' +
            '    </div>');

        $("body").append(testElement);
    });

    // Tear down the test element
    afterEach(function () {
        $(".conversation").remove();
    });

    // Perform tests
    it('scroll to bottom', function () {
        // Don't use '$(".messages")' for spying. It is not an existing object, but the  result of a jQuery selector
        spyOn($.fn, "animate");
        kyubot.scrollToBottom();

        var scrollDiv = $(".messages");
        var scrollTopArg = {scrollTop: scrollHeight};
        console.log("Scrolled to bottom of 'messages' div");
        expect(scrollDiv.animate).toHaveBeenCalledWith(scrollTopArg, 1000);
    });

    it('add message to conversation', function () {
        var type = "messageType";
        var content = "messageContent";
        spyOn(kyubot, "scrollToBottom")
        kyubot.addMessage(type, content);

        var addedContent = $(".messages").find(".message").find("." + type).text();
        console.log("Added content: " + addedContent);
        expect("." + type).toBeInDOM();
        expect(addedContent).toBe(content);
        expect(kyubot.scrollToBottom).toHaveBeenCalled();
    });

    it('add typing indicator', function () {
        spyOn(kyubot, "scrollToBottom")
        kyubot.addTypingIndicator();

        console.log("Added typing indicator");
        expect(".typing-indicator").toBeInDOM();
        expect(kyubot.scrollToBottom).toHaveBeenCalled();
    });

    it('remove typing indicator', function () {
        kyubot.addTypingIndicator();
        kyubot.removeTypingIndicator();

        console.log("Removed typing indicator");
        expect(".typing-indicator").not.toBeInDOM();
    });

    it('update conversation', function () {
        var testContext = "test context to be stored";
        var reply = "reply to add to the conversation";
        var context = {"test": testContext};
        var data = {"output": {"text": [reply]}, "context": context};
        spyOn(kyubot, "removeTypingIndicator");
        spyOn(kyubot, "addMessage")

        kyubot.updateConversation(data);

        console.log("Updated conversation");
        expect($("#watson_context").attr("value")).toBe(JSON.stringify(context));
        expect(kyubot.removeTypingIndicator).toHaveBeenCalled();
        expect(kyubot.addMessage).toHaveBeenCalledWith("kyubot", reply);
    });
});