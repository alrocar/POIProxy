/* Copyright (C) 2011 Alberto Romeu Carrasco
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 *   
 *   author: Alberto Romeu Carrasco (alberto@alrocar.es)
 */

package es.alrocar.map.vector.provider.driver.twitter;

import es.prodevelop.gvsig.mini.geom.Point;

public class TwitterPoint extends Point {

	private String imageURL;
	private String createdAt;
	private String fromUser;
	private String text;
	private String toUserId;
	private String location;
	private String geo;

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		if (geo == null)
			updateCoordinates(location);
		this.location = location;
	}

	public String getGeo() {
		return geo;
	}

	public void setGeo(String geo) {
		updateCoordinates(geo);
		this.geo = geo;
	}

	public void updateCoordinates(String loc) {
		try {
			if (loc != null && loc.contains(",")) {
				String[] point = loc.split(",");
				this.setX(Double.parseDouble(point[1]));
				this.setY(Double.parseDouble(point[0]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
