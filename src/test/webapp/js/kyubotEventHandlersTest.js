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

    it('close overlay by click on button', function() {
        spyOn(kyubot, "hideOverlay");
        spyEvent = spyOnEvent('#overlay_close', 'click');

        addOverlayHandlers();
        $("#overlay_close").trigger('click');

        console.log("Overlay closed by click on button");
        expect(spyEvent).toHaveBeenTriggered();
        expect(kyubot.hideOverlay).toHaveBeenCalled();
    });

    it('close overlay on click outside of the overlay content area', function() {
        spyOn(kyubot, "hideOverlay");
        var click = $.Event("click");
        var overlay = $("#overlay");
        click.target = overlay[0];

        addOverlayHandlers();
        overlay.trigger(click);
        console.log("Overlay closed - click was outside of the overlay content area");
        expect(kyubot.hideOverlay).toHaveBeenCalled();
    });

    it('do not close overlay on click inside of the overlay content area', function() {
        spyOn(kyubot, "hideOverlay");
        var click = $.Event("click");
        click.target = $("#overlay_content_box")[0];

        addOverlayHandlers();
        $("#overlay").trigger(click);
        console.log("Overlay not closed - click was inside of the overlay content area");
        expect(kyubot.hideOverlay).not.toHaveBeenCalled();
    });

    it('display overlay', function() {
        spyOn(kyubot, "displayOverlay");
        $("#overlay_content_box").wrapInner("<a href='" + overlayUrl + "'>display overlay</a>");

        addOverlayHandlers();
        $("a").trigger('click');
        console.log("Displayed overlay");
        expect(kyubot.displayOverlay).toHaveBeenCalled();
    });

});