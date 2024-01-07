package me.kimihiqq.domain.KeyValue

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "key_value")
class KeyValue(
    @Id @Column(name = "key_column")
    val key: String,
    @Column(name = "value_column")
    var value: String?
) {
    constructor() : this("", null)
}
