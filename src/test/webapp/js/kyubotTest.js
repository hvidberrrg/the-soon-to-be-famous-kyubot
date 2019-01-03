describe('KyuBOT Unit Tests', function () {
    // Define a local Kyubot instance for the tests in this spec  - and don't call it 'kyubot' as it will
    // overwrite the instance created by 'kyubotEventHandlers.js' (and this instance is relied upon by other
    // tests (and the order in which specs are executed can't be guaranteed)).
    var localKyubot;

    beforeAll(function () {
        // Initialize the kyubot
        localKyubot = new Kyubot();
    });

    beforeEach(function () {
        // Add an element to render into... jasmine.Fixtures.cleanUp() is automatically called between tests
        fixtures.set(conversation);
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

    it('submit query to watson', function() {
        var testInput = "some input string";
        $("#inputText").val(testInput);
        var form = $("#kyubotQuery");
        var formData = form.serialize();
        var event = $.Event("submit");

        spyOn(localKyubot, "addMessage");
        spyOn(localKyubot, "addTypingIndicator");
        spyOn(localKyubot, "postToConversation");
        spyOn(event, "preventDefault");

        localKyubot.submitQueryToWatson(form, event);
        console.log("Submitted query to Watson");
        expect(localKyubot.addMessage).toHaveBeenCalledWith("user", testInput);
        expect(localKyubot.addTypingIndicator).toHaveBeenCalled();
        expect(localKyubot.postToConversation).toHaveBeenCalledWith(formData);
        expect(event.preventDefault).toHaveBeenCalled();
    });

    it('do not submit empty query to watson', function() {
        var testInput = "";
        $("#inputText").val(testInput);
        var form = $("#kyubotQuery");
        var event = $.Event("submit");

        spyOn(localKyubot, "addMessage");
        spyOn(localKyubot, "addTypingIndicator");
        spyOn(localKyubot, "postToConversation");
        spyOn(event, "preventDefault");

        localKyubot.submitQueryToWatson(form, event);
        console.log("Didn't submitted empty query to Watson");
        expect(localKyubot.addMessage).not.toHaveBeenCalled();
        expect(localKyubot.addTypingIndicator).not.toHaveBeenCalled();
        expect(localKyubot.postToConversation).not.toHaveBeenCalled();
        expect(event.preventDefault).toHaveBeenCalled();
    });

    it('display overlay', function(done) {
        var event = $.Event("click");
        spyOn(event, "preventDefault");
        spyOn($.fn, "load").and.callThrough();
        spyOn($.fn, "fadeIn").and.callThrough();
        spyOn($.fn, "scrollTop").and.callThrough();

        localKyubot.displayOverlay(event, overlayUrl);
        // Wait for 'displayOverlay' to be done (as it uses an asynchronous callback to display the overlay after the url is loaded)
        setTimeout(function() {
            console.log("Displayed overlay");
            var overlayContent = $("#overlay_content");
            var overlay = $("#overlay");
            expect(event.preventDefault).toHaveBeenCalled();
            expect(overlayContent.load).toHaveBeenCalledWith(overlayUrl, jasmine.any(Function));
            expect(overlay.fadeIn).toHaveBeenCalledWith(300);
            expect(overlay.scrollTop).toHaveBeenCalledWith(0);
            done();
        }, 100);
    });

    it('hide overlay', function() {
        spyOn($.fn, "fadeOut");

        localKyubot.hideOverlay();
        console.log("Hid overlay");
        var overlay = $("#overlay");
        expect(overlay.fadeOut).toHaveBeenCalledWith('slow');
    });

});