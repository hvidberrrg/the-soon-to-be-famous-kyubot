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
        spyOn(kyubot, "scrollToBottom");
        kyubot.addMessage(type, content);

        var addedContent = $(".messages").find(".message").find("." + type).text();
        console.log("Added content: " + addedContent);
        expect("." + type).toBeInDOM();
        expect(addedContent).toBe(content);
        expect(kyubot.scrollToBottom).toHaveBeenCalled();
    });

    it('add typing indicator', function () {
        spyOn(kyubot, "scrollToBottom");
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
        spyOn(kyubot, "removeTypingIndicator");
        spyOn(kyubot, "addMessage");
        kyubot.updateConversation(kyubotResponse);

        console.log("Updated conversation");
        expect($("#watson_context").attr("value")).toBe(JSON.stringify(kyubotContext));
        expect(kyubot.removeTypingIndicator).toHaveBeenCalled();
        expect(kyubot.addMessage).toHaveBeenCalledWith("kyubot", kyubotReply);
    });

    it('post to conversation', function() {
        installAjaxMock();
        spyOn(kyubot, "post").and.callThrough();
        spyOn(kyubot, "updateConversation").and.callThrough();
        kyubot.postToConversation("userInput");

        var addedReply = $(".messages").find(".message").find(".kyubot").text();
        console.log("Posted '" + addedReply + "' to conversation");
        // Should test on the exact callback function but get a mismatch - hence 'jasmine.any(Function)'...
        expect(kyubot.post).toHaveBeenCalledWith(conversationUrl, "userInput", jasmine.any(Function));
        // ... instead, ensure the callback has been executed as expected.
        expect(kyubot.updateConversation).toHaveBeenCalledWith(kyubotResponse);
        expect(addedReply).toBe(kyubotReply);

        uninstallAjaxMock();
    });

    it('ajax post', function () {
        installAjaxMock();
        var callbackSpy = jasmine.createSpy("callback")
        kyubot.post(conversationUrl, "testData", callbackSpy);

        console.log("Performed AJAX post");
        var request = jasmine.Ajax.requests.mostRecent();
        expect(request.url).toBe(conversationUrl);
        expect(request.method).toBe("POST");
        expect(request.params).toBe("testData");
        expect(callbackSpy).toHaveBeenCalled();

        uninstallAjaxMock();
    });

    it('initiate conversation', function() {
        spyOn(kyubot, "addTypingIndicator");
        spyOn(kyubot, "postToConversation");
        kyubot.initiateConversation();

        console.log("Initiating conversation");
        expect(kyubot.addTypingIndicator).toHaveBeenCalled();
        expect(kyubot.postToConversation).toHaveBeenCalledWith("");
    });
});