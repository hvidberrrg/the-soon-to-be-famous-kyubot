describe('KyuBOT Event Handler Tests', function () {
    // The event handler tests below rely on the Kyubot instance created by 'kyubotEventHandlers.js'

    beforeEach(function () {
        // Add an element to render into... jasmine.Fixtures.cleanUp() is automatically called between tests
        fixtures.set(conversation);
    });

    it('initialize kyubot', function() {
        // Define the spies...
        spyOn(kyubot, "initiateConversation");
        spyOn(kyubot, "submitQueryToWatson");
        // .. and prepare a submit event that prevents the default form behaviour
        //(i.e. an event that doesn't redirect to '/conversation')
        var event = $.Event("submit");
        event.preventDefault();

        initKyubot();
        // Trigger a form submit event
        $("#kyubotQuery").trigger(event);

        console.log("Kyubot initialized");
        expect(kyubot.initiateConversation).toHaveBeenCalled();
        expect(kyubot.submitQueryToWatson).toHaveBeenCalled();
    });

});