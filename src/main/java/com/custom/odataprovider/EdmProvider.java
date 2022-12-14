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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;

public class EdmProvider extends CsdlAbstractEdmProvider {

  // Service Namespace
  public static final String NAMESPACE = "olingo.odata.sample";

  // EDM Container
  public static final String CONTAINER_NAME = "Container";
  public static final FullQualifiedName CONTAINER_FQN = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

  // Entity Types Names
  public static final FullQualifiedName ET_CAR = new FullQualifiedName(NAMESPACE, "Car");
  public static final FullQualifiedName ET_MANUFACTURER = new FullQualifiedName(NAMESPACE, "Manufacturer");

  // Complex Type Names
  public static final FullQualifiedName CT_ADDRESS = new FullQualifiedName(NAMESPACE, "Address");

  // Entity Set Names
  public static final String ES_CARS_NAME = "Cars";
  public static final String ES_MANUFACTURER_NAME = "Manufacturers";

  public String NAME;
  public  FullQualifiedName ET_NAME;
  public String ES_NAME;
  public EdmProvider(String name,FullQualifiedName fullName ) {
    NAME = name;
    ET_NAME = fullName;
    ES_NAME = name+"s";
  }

  @Override
  public CsdlEntityType getEntityType(final FullQualifiedName entityTypeName) throws ODataException {
      return new CsdlEntityType()
          .setName(ET_NAME.getName())
          .setKey(Arrays.asList(
              new CsdlPropertyRef().setName("count")))
          .setProperties(
              Arrays.asList(
                new CsdlProperty().setName("count").setType(EdmPrimitiveTypeKind.Int16.getFullQualifiedName()),
                  new CsdlProperty().setName("equipment").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
                  new CsdlProperty().setName("description").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
                  new CsdlProperty().setName("schedstartdate").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),

                  new CsdlProperty().setName("reportedby").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
                  new CsdlProperty().setName("organization").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
                  new CsdlProperty().setName("workordernum").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
                  new CsdlProperty().setName("duedate").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),

                  new CsdlProperty().setName("workorderstatus").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
                  new CsdlProperty().setName("equipmentorg").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
                  new CsdlProperty().setName("workorderrtype").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
                  new CsdlProperty().setName("equipsystype").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName())
                  )
          );
  }

  public CsdlComplexType getComplexType(final FullQualifiedName complexTypeName) throws ODataException {
    if (CT_ADDRESS.equals(complexTypeName)) {
      return new CsdlComplexType().setName(CT_ADDRESS.getName()).setProperties(Arrays.asList(
          new CsdlProperty().setName("Street").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
          new CsdlProperty().setName("City").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
          new CsdlProperty().setName("ZipCode").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()),
          new CsdlProperty().setName("Country").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName())
          ));
    }
    return null;
  }

  @Override
  public CsdlEntitySet getEntitySet(final FullQualifiedName entityContainer, final String entitySetName)
      throws ODataException {
    if (CONTAINER_FQN.equals(entityContainer)) {
        return new CsdlEntitySet()
            .setName(ES_NAME)
            .setType(ET_NAME);
      } 

    return null;
  }

  @Override
  public List<CsdlSchema> getSchemas() throws ODataException {
    List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
    CsdlSchema schema = new CsdlSchema();
    schema.setNamespace(NAMESPACE);
    // EntityTypes
    List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
    entityTypes.add(getEntityType(ET_NAME));
    schema.setEntityTypes(entityTypes);

    // ComplexTypes
    // List<CsdlComplexType> complexTypes = new ArrayList<CsdlComplexType>();
    // complexTypes.add(getComplexType(CT_ADDRESS));
    // schema.setComplexTypes(complexTypes);

    // EntityContainer
    schema.setEntityContainer(getEntityContainer());
    schemas.add(schema);

    return schemas;
  }

  @Override
  public CsdlEntityContainer getEntityContainer() throws ODataException {
    CsdlEntityContainer container = new CsdlEntityContainer();
    container.setName(CONTAINER_FQN.getName());

    // EntitySets
    List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
    container.setEntitySets(entitySets);
    entitySets.add(getEntitySet(CONTAINER_FQN, ES_NAME));
    // entitySets.add(getEntitySet(CONTAINER_FQN, ES_MANUFACTURER_NAME));

    return container;
  }

  @Override
  public CsdlEntityContainerInfo getEntityContainerInfo(final FullQualifiedName entityContainerName)
          throws ODataException {
    if (entityContainerName == null || CONTAINER_FQN.equals(entityContainerName)) {
      return new CsdlEntityContainerInfo().setContainerName(CONTAINER_FQN);
    }
    return null;
  }
}
