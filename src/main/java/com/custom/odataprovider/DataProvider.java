/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.custom.odataprovider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

import org.json.*;


public class DataProvider {

  private final Map<String, EntityCollection> data;

  public DataProvider(String entitysetname, JSONArray records, FullQualifiedName ET_NAME) {
    data = new HashMap<String, EntityCollection>();
    data.put(entitysetname, createCars(records,ET_NAME));
  }

  public EntityCollection readAll(EdmEntitySet edmEntitySet) {
    return data.get(edmEntitySet.getName());
  }

  public Entity read(final EdmEntitySet edmEntitySet, final List<UriParameter> keys) throws DataProviderException {
    final EdmEntityType entityType = edmEntitySet.getEntityType();
    final EntityCollection entitySet = data.get(edmEntitySet.getName());
    if (entitySet == null) {
      return null;
    } else {
      try {
        for (final Entity entity : entitySet.getEntities()) {
          boolean found = true;
          for (final UriParameter key : keys) {
            final EdmProperty property = (EdmProperty) entityType.getProperty(key.getName());
            final EdmPrimitiveType type = (EdmPrimitiveType) property.getType();
            if (!type.valueToString(entity.getProperty(key.getName()).getValue(),
                property.isNullable(), property.getMaxLength(), property.getPrecision(), property.getScale(),
                property.isUnicode())
                .equals(key.getText())) {
              found = false;
              break;
            }
          }
          if (found) {
            return entity;
          }
        }
        return null;
      } catch (final EdmPrimitiveTypeException e) {
        throw new DataProviderException("Wrong key!", e);
      }
    }
  }

  public static class DataProviderException extends ODataException {
    private static final long serialVersionUID = 5098059649321796156L;

    public DataProviderException(String message, Throwable throwable) {
      super(message, throwable);
    }

    public DataProviderException(String message) {
      super(message);
    }
  }

  private EntityCollection createCars(JSONArray records,FullQualifiedName ET_NAME) {
    EntityCollection entitySet = new EntityCollection();
    // Entity el = new Entity()
    //     .addProperty(createPrimitive("Id", 1))
    //     .addProperty(createPrimitive("Model", "F1 W03"))
    //     .addProperty(createPrimitive("ModelYear", "2012"))
    //     .addProperty(createPrimitive("Price", 189189.43))
    //     .addProperty(createPrimitive("Currency", "EUR"));
    // el.setId(createId(CarsEdmProvider.ES_CARS_NAME, 1));
    // entitySet.getEntities().add(el);
    Entity el = new Entity();

    for(int i = 0; i < records.length(); i++)
    {
          JSONObject object = records.getJSONObject(i);
          JSONArray fields = object.getJSONArray("DATAFIELD");
          el = new Entity()
          .addProperty(createPrimitive("count", i));
          for(int j = 0; j < fields.length(); j++)
    {
      JSONObject field = fields.getJSONObject(j);
        el.addProperty(createPrimitive(field.getString("FIELDNAME"), field.getString("FIELDVALUE")));
    }
        entitySet.getEntities().add(el);
          //Iterate through the elements of the array i.
          //Get thier value.
          //Get the value for the first element and the value for the last element.
    }

    for (Entity entity:entitySet.getEntities()) {
      entity.setType(ET_NAME.getFullQualifiedNameAsString());
    }
    return entitySet;
  }

  private Property createPrimitive(final String name, final Object value) {
    return new Property(null, name, ValueType.PRIMITIVE, value);
  }

  private URI createId(String entitySetName, Object id) {
    try {
      return new URI(entitySetName + "(" + String.valueOf(id) + ")");
    } catch (URISyntaxException e) {
      throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
    }
  }
}
