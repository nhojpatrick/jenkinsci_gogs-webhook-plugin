package org.jenkinsci.plugins.gogs;

import hudson.model.Item;
import jenkins.model.Jenkins;

import java.util.logging.Logger;

class GogsUtils {

	private final static Logger LOGGER = Logger.getLogger(GogsUtils.class.getName());

	private GogsUtils() {
	}

	/**
	 * Search in Jenkins for a item with type T based on the job name
	 * @param jobName job to find, for jobs inside a folder use : {@literal <folder>/<folder>/<jobName>}
	 * @return the Job matching the given name, or {@code null} when not found
	 */
	static <T extends Item> T find(String jobName, Class<T> type) {
		Jenkins jenkins = Jenkins.getActiveInstance();
		// direct search, can be used to find folder based items <folder>/<folder>/<jobName>
		T item = jenkins.getItemByFullName(jobName, type);
		LOGGER.fine("GogsWebHook found Job '" + jobName + "' ByFullName " + (item != null));
		if (item == null) {
			// not found in a direct search, search in all items since the item might be in a folder but given without folder structure
			// (to keep it backwards compatible)
			for (T allItem : jenkins.getAllItems(type)) {
				LOGGER.fine("GogsWebHook searching for Job '" + jobName + "' checking '" + allItem.getName() + '"');
				if (allItem.getName().equals(jobName)) {
					LOGGER.fine("GogsWebHook found Job '" + jobName + "' " + (allItem != null) + " Item '" + allItem + "'");
					item = allItem;
					break;
				}
			}
		}
		LOGGER.fine("GogsWebHook Job '" + jobName + "' found " + (item != null));
		return item;
	}

}
