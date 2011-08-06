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

package es.alrocar.map.vector.provider.driver.flickr;

import net.jeremybrooks.jinx.dto.Photo;
import es.prodevelop.gvsig.mini.geom.Point;

public class FlickrPoint extends Point {

	private Photo p;

	public FlickrPoint(Photo p) {
		super(Double.parseDouble(p.getLongitude()), Double.parseDouble(p
				.getLatitude()));
		this.p = p;
	}

	public Photo getPhoto() {
		return p;
	}
}
