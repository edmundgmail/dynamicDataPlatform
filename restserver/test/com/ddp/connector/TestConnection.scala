package com.ddp.connector

import com.ddp.dataplatform.DataPlatformConnectionService
import com.ddp.models.NewDataSourceJDBC
import com.ddp.utils.Testing

import scala.concurrent.Future

class TestConnection extends Testing{ {
  val fixture = new DataPlatformConnectionService
  it("test Connection of JDBC") {
    val newDataSourceJDBC = NewDataSourceJDBC(
      url =  "jdbc:mysql://localhost:3306/ddp;user=root;password=password",
      driver = "com.mysql.jdbc.Driver",
      user = "root",
      pass = "password",
      sql = ""
    )
      //fixture.testConnection(newDataSourceJDBC)
  }
}
}
