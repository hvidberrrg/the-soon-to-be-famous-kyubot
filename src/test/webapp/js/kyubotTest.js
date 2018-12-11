describe('KyuBOT Unit Tests', function () {
    // Define a local Kyubot instance for the tests in this spec  - and don't call it 'kyubot' as it will
    // overwrite the instance created by 'kyubotEventHandlers.js' (and this instance is relied upon by other
    // tests (and the order in which specs are executed can't be guaranteed)).
    var localKyubot;
    var scrollHeight;

    // Initialize the kyubot
    beforeAll(function () {
        localKyubot = new Kyubot();
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
        localKyubot.scrollToBottom();

        var scrollDiv = $(".messages");
        var scrollTopArg = {scrollTop: scrollHeight};
        console.log("Scrolled to bottom of 'messages' div");
        expect(scrollDiv.animate).toHaveBeenCalledWith(scrollTopArg, 1000);
    });

    it('add message to conversation', function () {
        var type = "messageType";
        var content = "messageContent";
        spyOn(localKyubot, "scrollToBottom");
        localKyubot.addMessage(type, content);

        var addedContent = $(".messages").find(".message").find("." + type).text();
        console.log("Added content: " + addedContent);
        expect("." + type).toBeInDOM();
        expect(addedContent).toBe(content);
        expect(localKyubot.scrollToBottom).toHaveBeenCalled();
    });

    it('add typing indicator', function () {
        spyOn(localKyubot, "scrollToBottom");
        localKyubot.addTypingIndicator();

        console.log("Added typing indicator");
        expect(".typing-indicator").toBeInDOM();
        expect(localKyubot.scrollToBottom).toHaveBeenCalled();
    });

    it('remove typing indicator', function () {
        localKyubot.addTypingIndicator();
        localKyubot.removeTypingIndicator();

        console.log("Removed typing indicator");
        expect(".typing-indicator").not.toBeInDOM();
    });

    it('update conversation', function () {
        spyOn(localKyubot, "removeTypingIndicator");
        spyOn(localKyubot, "addMessage");
        localKyubot.updateConversation(kyubotResponse);

        console.log("Updated conversation");
        expect($("#watson_context").attr("value")).toBe(JSON.stringify(kyubotContext));
        expect(localKyubot.removeTypingIndicator).toHaveBeenCalled();
        expect(localKyubot.addMessage).toHaveBeenCalledWith("kyubot", kyubotReply);
    });

    it('post to conversation', function() {
        installAjaxMock();
        spyOn(localKyubot, "post").and.callThrough();
        spyOn(localKyubot, "updateConversation").and.callThrough();
        localKyubot.postToConversation("userInput");

        var addedReply = $(".messages").find(".message").find(".kyubot").text();
        console.log("Posted '" + addedReply + "' to conversation");
        // Should test on the exact callback function but get a mismatch - hence 'jasmine.any(Function)'...
        expect(localKyubot.post).toHaveBeenCalledWith(conversationUrl, "userInput", jasmine.any(Function));
        // ... instead, ensure the callback has been executed as expected.
        expect(localKyubot.updateConversation).toHaveBeenCalledWith(kyubotResponse);
        expect(addedReply).toBe(kyubotReply);

        uninstallAjaxMock();
    });

    it('ajax post', function () {
        installAjaxMock();
        var callbackSpy = jasmine.createSpy("callback");
        localKyubot.post(conversationUrl, "testData", callbackSpy);

        console.log("Performed AJAX post");
        var request = jasmine.Ajax.requests.mostRecent();
        expect(request.url).toBe(conversationUrl);
        expect(request.method).toBe("POST");
        expect(request.params).toBe("testData");
        expect(callbackSpy).toHaveBeenCalled();

        uninstallAjaxMock();
    });

    it('initiate conversation', function() {
        spyOn(localKyubot, "addTypingIndicator");
        spyOn(localKyubot, "postToConversation");
        localKyubot.initiateConversation();

        console.log("Initiating conversation");
        expect(localKyubot.addTypingIndicator).toHaveBeenCalled();
        expect(localKyubot.postToConversation).toHaveBeenCalledWith("");
    });
});