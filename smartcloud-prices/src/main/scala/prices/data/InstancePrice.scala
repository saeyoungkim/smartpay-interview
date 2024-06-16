package prices.data

import java.util.Date

final case class InstancePrice(kind: InstanceKind, price: Double, timestamp: Date)
