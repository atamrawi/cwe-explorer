package com.ensoftcorp.open.cwe;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin implements BundleActivator {

	/**
	 * A static instance of {@link Activator}.
	 */
	private static Activator ACTIVATOR_PLUGIN;
	
	@Override
	public void start(BundleContext context) throws Exception {
        super.start(context);
        ACTIVATOR_PLUGIN = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		ACTIVATOR_PLUGIN = null;
        super.stop(context);
	}
	
    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
	public static Activator getDefault() {
		return ACTIVATOR_PLUGIN;
	}

}
