/*******************************************************************************
 * Copyright (c) 2006 Vladimir Silva and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vladimir Silva - initial API and implementation
 *******************************************************************************/
package org.eclipse.plugin.worldwind.utils;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.plugin.worldwind.Activator;
import org.eclipse.plugin.worldwind.views.WebBrowserView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

/**
 * A fancy tool tip class with a web browser to display tooltips from HTML.
 * @author vsilva
 *
 */
public class LayersToolTipSupport extends ColumnViewerToolTipSupport 
{
	IWorkbenchWindow window;
	
	GridData data = new GridData(200,200);
	
	protected LayersToolTipSupport(ColumnViewer viewer, int style,
			boolean manualActivation, IWorkbenchWindow window) 
	{
		super(viewer, style, manualActivation);
		this.window = window;
	}

	@Override
	protected Composite createToolTipContentArea(Event event, Composite parent) {
		final Display display = parent.getDisplay();
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout l = new GridLayout(1,false);
		l.horizontalSpacing=0;
		l.marginWidth=0;
		l.marginHeight=0;
		l.verticalSpacing=0;
		
		composite.setLayout(l);
		Browser browser = new Browser(composite,SWT.NONE);
		browser.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		browser.setText(getText(event));
		browser.setLayoutData(data);
		
	    browser.addLocationListener(new LocationListener(){

			public void changed(LocationEvent event) {
				;//processLocation (event);
			}

			public void changing(LocationEvent event) 
			{
				processLocation (event);
			}
	    	
	    });
		
		return composite;
	}

	/*
	 * When a link is clicked in the tool tip the page is displayed on the 
	 * web browser view
	 * @param event
	 */
	private void processLocation (LocationEvent event) 
	{
		// invalid wb window?
		if ( window == null ) {
			return;
		}
		
		String location = event.location;
		
		// Display location on WB view
		// Grab web browser view
		WebBrowserView view = (WebBrowserView)Activator.getView(window
				, WebBrowserView.ID);
		
		view.setUrl(location);
		try {
			window.getActivePage().showView(WebBrowserView.ID);
		} catch (PartInitException e) {
			// should not happen
			e.printStackTrace();
		}
		event.doit = false;
	}

	public boolean isHideOnMouseDown() {
		return false;
	}
	
	public void setTipSize (int width, int height) {
		data = new GridData(width, height);
	}
	
	
	public static final LayersToolTipSupport enablefor(ColumnViewer viewer
			, int style, IWorkbenchWindow window) 
	{
		return new LayersToolTipSupport(viewer, style, false, window);
	}
}
