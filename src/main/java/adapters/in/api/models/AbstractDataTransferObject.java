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

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

/**
 * This class specifies the basic requirements, that a Model should fulfill to be
 * used as a resource or a sub-resource.
 */
public abstract class AbstractDataTransferObject
{

	/**
	 * A unique identifier for the model object
	 */
	@PositiveOrZero
	protected long id;

	/**
	 * The id of the primary resource the model is related to, when the model is being used as a sub-resource
	 */
	@PositiveOrZero
	private long primaryId;
	protected Link selfLink;


	protected AbstractDataTransferObject( )
	{
		this.selfLink = new Link( );
	}

	public long getId( )
	{
		return this.id;
	}

	public void setId( final long id )
	{
		this.id = id;
	}

	public Link getSelfLink( )
	{
		return selfLink;
	}

	public void setSelfLink( Link selfLink )
	{
		this.selfLink = selfLink;
	}

	/**
	 * @return the {@link AbstractDataTransferObject#primaryId} {@link Long}
	 */
	@JsonIgnore
	public long getPrimaryId( )
	{
		return primaryId;
	}

	/**
	 * @param primaryId {@link Long} - the id of the primary resource, this model is related to
	 *                  Set the {@link AbstractDataTransferObject#primaryId} to the provided value
	 */
	@JsonIgnore
	public void setPrimaryId( final long primaryId )
	{
		this.primaryId = primaryId;
	}

	/**
	 * Creates and returns a copy of the model
	 *
	 * @throws CloneNotSupportedException - if the model couldn't be cloned
	 */
	@Override
	public Object clone( ) throws CloneNotSupportedException
	{
		return super.clone( );
	}
}