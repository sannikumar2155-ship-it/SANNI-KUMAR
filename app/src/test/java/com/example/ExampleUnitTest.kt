package com.example

import com.example.model.AccountStatus
import com.example.model.RoomCategory
import com.example.model.VipTier
import com.example.ui.viewmodel.LoveraViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testAuthenticationAndProfile() {
        val vm = LoveraViewModel()
        assertNotNull(vm.currentUser.value)
        assertEquals(true, vm.isLoggedIn.value)

        vm.loginWithPhone("+91 99999 88888", "7721")
        assertEquals("+91 99999 88888", vm.currentUser.value?.phoneNumber)
    }

    @Test
    fun testPartyRoomJoiningAndMic() {
        val vm = LoveraViewModel()
        val firstRoom = vm.partyRooms.value.first()
        vm.joinRoom(firstRoom)
        assertNotNull(vm.activeRoom.value)

        // Take seat 3
        vm.takeSeat(3)
        val seated = vm.activeRoom.value?.seats?.get(3)
        assertNotNull(seated?.user)
        assertEquals(vm.currentUser.value?.id, seated?.user?.id)

        // Step down
        vm.leaveSeat()
        val emptySeat = vm.activeRoom.value?.seats?.get(3)
        assertNull(emptySeat?.user)
    }

    @Test
    fun testSuspendAndTerminateServices() {
        val vm = LoveraViewModel()
        assertEquals(AccountStatus.ACTIVE, vm.currentUser.value?.accountStatus)

        // Suspend account temporarily
        vm.suspendAccountTemporarily()
        assertEquals(AccountStatus.SUSPENDED_TEMPORARY, vm.currentUser.value?.accountStatus)

        // Reactivate
        vm.reactivateAccount()
        assertEquals(AccountStatus.ACTIVE, vm.currentUser.value?.accountStatus)

        // Suspend paid services
        vm.suspendPaidServices()
        assertEquals(false, vm.currentUser.value?.paidServiceActive)

        // Terminate account and services
        vm.terminateAccountAndServices()
        assertEquals(AccountStatus.TERMINATED, vm.currentUser.value?.accountStatus)
        assertEquals(0, vm.currentUser.value?.coins)
        assertEquals(false, vm.isLoggedIn.value)
    }
}
