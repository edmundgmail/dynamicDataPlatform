package com.ddp.hbase

import com.ddp.logging.Logging
import com.ddp.utils.Testing

class TestHBaseShapeless extends Testing with Logging{
  object Model extends HBaseTable("sandra_example") {
    object Users extends StandardFamily("users", "cf1") {
      val name = StringColumn("cf1", "name")
      val email = StringColumn("cf1", "email")
    }
  }

  import Model._

  it("Test adding rows") {
  }
}
