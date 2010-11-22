/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/ade/sitemap/client/hoverbar/Attic/CmsDeleteMenuEntry.java,v $
 * Date   : $Date: 2010/11/18 15:32:41 $
 * Version: $Revision: 1.2 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (C) 2002 - 2009 Alkacon Software (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.ade.sitemap.client.hoverbar;

import org.opencms.ade.sitemap.client.CmsSitemapView;
import org.opencms.ade.sitemap.client.Messages;
import org.opencms.ade.sitemap.client.control.CmsSitemapController;
import org.opencms.ade.sitemap.client.ui.CmsLinkWarningDialog;
import org.opencms.ade.sitemap.shared.CmsClientSitemapEntry;
import org.opencms.ade.sitemap.shared.CmsSitemapBrokenLinkBean;
import org.opencms.gwt.client.ui.CmsConfirmDialog;
import org.opencms.gwt.client.ui.I_CmsConfirmDialogHandler;
import org.opencms.gwt.client.ui.css.I_CmsImageBundle;
import org.opencms.util.CmsPair;
import org.opencms.util.CmsUUID;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Sitemap context menu delete entry.<p>
 * 
 * @author Tobias Herrmann
 * 
 * @version $Revision: 1.2 $
 * 
 * @since 8.0.0
 */
public class CmsDeleteMenuEntry extends A_CmsSitemapMenuEntry {

    /**
     * Constructor.<p>
     * 
     * @param hoverbar the hoverbar 
     */
    public CmsDeleteMenuEntry(CmsSitemapHoverbar hoverbar) {

        super(hoverbar);
        setImageClass(I_CmsImageBundle.INSTANCE.contextMenuIcons().delete());
        setLabel(Messages.get().key(Messages.GUI_HOVERBAR_DELETE_0));
        setActive(true);
        setCommand(new Command() {

            /**
             * @see com.google.gwt.user.client.Command#execute()
             */
            public void execute() {

                final String sitePath = getHoverbar().getSitePath();
                final CmsSitemapController controller = getHoverbar().getController();
                CmsPair<List<CmsClientSitemapEntry>, List<CmsClientSitemapEntry>> openAndClosed = CmsSitemapView.getInstance().getOpenAndClosedDescendants(
                    sitePath);
                List<CmsClientSitemapEntry> openEntries = openAndClosed.getFirst();
                List<CmsClientSitemapEntry> closedEntries = openAndClosed.getSecond();
                List<CmsUUID> open = getIds(openEntries);
                List<CmsUUID> closed = getIds(closedEntries);
                controller.getBrokenLinks(open, closed, new AsyncCallback<List<CmsSitemapBrokenLinkBean>>() {

                    /**
                     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
                     */
                    public void onFailure(Throwable caught) {

                        // do nothing; will never be called
                    }

                    /**
                     * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
                     */
                    public void onSuccess(List<CmsSitemapBrokenLinkBean> result) {

                        if (result.size() > 0) {
                            I_CmsConfirmDialogHandler handler = new I_CmsConfirmDialogHandler() {

                                /**
                                 * @see org.opencms.gwt.client.ui.I_CmsCloseDialogHandler#onClose()
                                 */
                                public void onClose() {

                                    // do nothing 
                                }

                                /**
                                 * @see org.opencms.gwt.client.ui.I_CmsConfirmDialogHandler#onOk()
                                 */
                                public void onOk() {

                                    controller.delete(sitePath);
                                }
                            };
                            CmsLinkWarningDialog dialog = new CmsLinkWarningDialog(handler, result);
                            dialog.center();

                        } else {

                            if (controller.getEntry(sitePath).getSubEntries().isEmpty()) {
                                controller.delete(sitePath);
                                return;
                            }
                            // show the dialog only if the entry has children 
                            CmsConfirmDialog dialog = new CmsConfirmDialog(Messages.get().key(
                                Messages.GUI_DIALOG_DELETE_TITLE_0), Messages.get().key(
                                Messages.GUI_DIALOG_DELETE_TEXT_0));
                            dialog.setHandler(new I_CmsConfirmDialogHandler() {

                                /**
                                 * @see org.opencms.gwt.client.ui.I_CmsCloseDialogHandler#onClose()
                                 */
                                public void onClose() {

                                    // do nothing
                                }

                                /**
                                 * @see org.opencms.gwt.client.ui.I_CmsConfirmDialogHandler#onOk()
                                 */
                                public void onOk() {

                                    controller.delete(sitePath);
                                }
                            });
                            dialog.center();
                        }
                    }

                });

            }
        });

    }

    /**
     * @see org.opencms.ade.sitemap.client.hoverbar.A_CmsSitemapMenuEntry#onShow(org.opencms.ade.sitemap.client.hoverbar.CmsHoverbarShowEvent)
     */
    @Override
    public void onShow(CmsHoverbarShowEvent event) {

        String sitePath = getHoverbar().getSitePath();
        CmsSitemapController controller = getHoverbar().getController();
        if (controller.isRoot(sitePath)) {
            setVisible(false);
        } else {
            setVisible(true);
        }
    }

    /**
     * Extracts the ids from a list of client sitemap entries.<p>
     * 
     * @param entries the sitemap entries
     * 
     * @return the ids of the sitemap entries 
     */
    protected List<CmsUUID> getIds(List<CmsClientSitemapEntry> entries) {

        List<CmsUUID> result = new ArrayList<CmsUUID>();
        for (CmsClientSitemapEntry entry : entries) {
            result.add(entry.getId());
        }
        return result;
    }
}