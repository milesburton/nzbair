
package com.mb.android.nzbAirPremium.ui.helper;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import com.mb.nzbair.providers.domain.UsenetPost;

public class UsenetPostConverter {

	public static Hashtable<String, String> convert(UsenetPost post) {

		final Hashtable<String, String> table = new Hashtable<String, String>();
		if (post.getCategoryText() != null) {
			table.put("Category", post.getCategoryText());
		}
		if (post.getFilesize() != null) {
			table.put("Filesize", FormatHelper.formatFileSizeBytes(post.getFilesize()));
		}
		if (post.getGroups() != null) {
			table.put("Groups", join(post.getGroups(), "\n"));
		}
		try {
			if (post.getUnixTimeAdded() != null) {
				table.put("Indexed", getDifferenceInDays(post.getUnixTimeAdded()));
			}
		} catch (final Exception ex) {
		}
		if (post.getMetadata() != null) {
			table.putAll(post.getMetadata());
		}

		return table;

	}

	public static String join(Collection<String> s, String delimiter) {
		final StringBuffer buffer = new StringBuffer();
		final Iterator<String> iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	private static String getDifferenceInDays(Long utcTime) {

		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(1);
		try {
			final Long now = System.currentTimeMillis() / 1000L;
			final Long differenceInSeconds = ((now - utcTime));
			final double differenceInDays = differenceInSeconds / 86400;
			if (differenceInDays < 1) {
				final double differenceInMinutes = differenceInSeconds / 60;
				if (differenceInMinutes < 60) {
					return nf.format(differenceInMinutes) + " minutes ago";
				} else {
					return nf.format(differenceInMinutes / 60) + " hours ago";
				}
			} else {
				return nf.format(differenceInDays) + " days ago";
			}

		} catch (final Exception ex) {
			ex.printStackTrace();
			return "Unknown";
		}
	}
}
