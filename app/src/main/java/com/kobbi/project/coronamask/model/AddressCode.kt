package com.kobbi.project.coronamask.model

enum class AddressCode(val fullName: String, val shortName: String) {
    SEOUL("서울특별시", "서울"),
    BUSAN("부산광역시", "부산"),
    INCHEON("인천광역시", "인천"),
    DAEGU("대구광역시", "대구"),
    GWANGJU("광주광역시", "광주"),
    DAEJUN("대전광역시", "대전"),
    ULSAN("울산광역시", "울산"),
    GYEONGGI("경기도", "경기"),
    GANGWON("강원도", "강원"),
    CHUNGBUK("충청북도", "충북"),
    CHUNGNAM("충청남도", "충남"),
    JEONBUK("전라북도", "전북"),
    JEONNAM("전라남도", "전남"),
    GYEONGBUK("경상북도", "경북"),
    GYEONGNAM("경상남도", "경남"),
    JAEJU("제주특별자치도", "제주");

    companion object {
        fun findAddressCode(name: String): AddressCode? {
            return values().firstOrNull {
                it.fullName == name
            }
        }
    }
}