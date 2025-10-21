package co.qredet.sdk

val EventBus = Bus().getBus()
class Events {

    class NfcReadResult (result: String) {
        private var result: String = ""

        init {
            this.result = result
        }

        fun getResult(): String {
            return this.result
        }

    }
}
