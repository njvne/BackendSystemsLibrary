/*
 * Copyright 2019 University of Applied Sciences Würzburg-Schweinfurt, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package adapters.in.api.models;

/**
 * The {@link Link} class represents a hypermedia link. It encapsulates the href, rel,
 * and type attributes that define the characteristics of a hyperlink.
 */
public class Link
{
    private String href;
    private String rel;
    private String type;

    /**
     * Constructs an empty {@link Link} object.
     */
    public Link()
    {

    }

    /**
     * Constructs a {@link Link} object with the specified href, rel, and type attributes.
     *
     * @param href The URL that the link points to.
     * @param rel  The relationship type of the link.
     * @param type The media type of the link target.
     */
    public Link(String href, String rel, String type)
    {
        this.href = href;
        this.rel = rel;
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public String getRel() {
        return rel;
    }

    public String getType() {
        return type;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Link{" +
                "href='" + href + '\'' +
                ", rel='" + rel + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}