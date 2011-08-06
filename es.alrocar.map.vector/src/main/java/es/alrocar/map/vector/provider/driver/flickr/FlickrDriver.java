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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import net.jeremybrooks.jinx.Jinx;
import net.jeremybrooks.jinx.JinxException;
import net.jeremybrooks.jinx.api.PhotosApi;
import net.jeremybrooks.jinx.dto.Photo;
import net.jeremybrooks.jinx.dto.Photos;
import net.jeremybrooks.jinx.dto.SearchParameters;
import es.alrocar.map.vector.provider.driver.impl.BaseDriver;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.utiles.Cancellable;

public class FlickrDriver extends BaseDriver {

	private String key = "87c99763bacd56eec1db7ff3d71e230e";
	private String secret = "4f455bdef057100f";
	
	public FlickrDriver() {
		Jinx.getInstance().init(key, secret);
		// this.provider = provider;
	}

	public ArrayList getData(int[] tile, Extent boundingBox,
			Cancellable cancellable) {
		SearchParameters param = new SearchParameters();
		String extent = getExtent(tile, boundingBox);
		param.setBbox(extent);
		param.setExtras("owner_name,geo,media,url_sq,url_t,url_s,url_m,url_z,url_o");
		param.setPerPage(20);
		param.setPage(1);
		param.setAccuracy("16");
		param.setSort("interestingness-desc");
		param.setMinUploadDate(new Date(1140981900));

		ArrayList points = null;
		try {
			if (cancellable != null && cancellable.getCanceled())
				return null;
			Photos p = PhotosApi.getInstance().search(param);
			System.out.println(p.getTotal());
			Iterator<Photo> it = p.getPhotos().iterator();

			points = new ArrayList(p.getPhotos().size());
			Photo p_;
			while (it.hasNext()) {
				try {
					if (cancellable != null && cancellable.getCanceled())
						return null;
					p_ = it.next();
//					System.out.println(p_.getTitle());
					Point f = new FlickrPoint(p_);
					points.add(f);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return points;
		} catch (JinxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return points;
		}
	}	
	
	public String getName() {
		return "flickr";
	}
}
