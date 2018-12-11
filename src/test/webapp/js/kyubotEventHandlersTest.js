describe('KyuBOT Event Handler Tests', function () {
    // The event handler tests below rely on the Kyubot instance created by 'kyubotEventHandlers.js'

    beforeAll(function () {
        installAjaxMock();

        // Add an element to render into
        $.holdReady(true); // .. and make sure the DOM is augmented before $(document).ready is called
        scrollHeight = 10000;
        testElement = $('<div class="conversation">\n' +
            '        <form class="chat" action="/conversation" method="post" id="kyubotQuery">\n' +
            '            <div class="messages" style="height:' + scrollHeight + 'px;">&nbsp;</div>\n' +
            '            <input type="text" id="inputText" name="input" placeholder="Enter your message here...">\n' +
            '            <input type="hidden" id="watson_context" name="context" value="">\n' +
            '            <input type="submit" value="Send">\n' +
            '        </form>\n' +
            '    </div>');

        $("body").append(testElement);
        $.holdReady(false);
    });

    afterAll(function () {
        // Tear down the test element
        $(".conversation").remove();

        uninstallAjaxMock();
    });

    it('document ready event', function (done) {
        // Define the 'initiateConversation' spy
        spyOn(kyubot, "initiateConversation");

        // Wait for 'initiateConversation' to be done...
        setTimeout(function() {
            console.log("Document ready event handled");
            expect(kyubot.initiateConversation).toHaveBeenCalled();
            done();
        }, 100);
    });

    it('submit form', function () {
        // Define the 'submitQueryToWatson' spy
        spyOn(kyubot, "submitQueryToWatson");
        // .. and prepare a submit event that prevents the default form behaviour
        //(i.e. an event that doesn't redirect to '/conversation')
        var event = $.Event("submit");
        event.preventDefault();

        // Trigger the form submit event
        $("#kyubotQuery").trigger(event);

        console.log("Kyubot form submit event handled");
        expect(kyubot.submitQueryToWatson).toHaveBeenCalled();
    });

});