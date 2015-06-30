/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stratio.sparkta.plugin.operator.lastValue

import java.io.{Serializable => JSerializable}
import scala.util.Try

import com.stratio.sparkta.sdk.TypeOp
import com.stratio.sparkta.sdk.TypeOp._
import com.stratio.sparkta.sdk.ValidatingPropertyMap._
import com.stratio.sparkta.sdk._

class LastValueOperator(name: String, properties: Map[String, JSerializable]) extends Operator(name, properties) {

  override val defaultTypeOperation = TypeOp.String

  private val inputField = if (properties.contains("inputField")) properties.getString("inputField", None) else None

  override val writeOperation = WriteOp.Set

  override def processMap(inputFields: Map[String, JSerializable]): Option[Any] =
    if ((inputField.isDefined) && (inputFields.contains(inputField.get))) {
      Some(inputFields.get(inputField.get).get)
    } else LastValueOperator.Some_Empty

  override def processReduce(values: Iterable[Option[Any]]): Option[Any] =
    Try(Some(transformValueByTypeOp(returnType, values.last.getOrElse(LastValueOperator.Empty))))
      .getOrElse(LastValueOperator.Some_Empty)
}

private object LastValueOperator {
  val Some_Empty = Some("")
  val Empty = ""
}
