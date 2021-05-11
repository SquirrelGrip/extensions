package com.github.squirrelgrip.jupiter.extension.systemexit

import com.github.squirrelgrip.jupiter.extension.systemexit.SystemExitPreventedException
import java.io.FileDescriptor
import java.net.InetAddress
import java.security.Permission

class DisallowExitSecurityManager(
    private val delegatedSecurityManager: SecurityManager?
) : SecurityManager() {
    var firstExitStatusCode: Int? = null
        private set
    var preventedSystemExitCount = 0
        private set

    /**
     * This is the one method we truly override in this class, all others are delegated.
     *
     * @param statusCode the exit status
     */
    override fun checkExit(statusCode: Int) {
        if (firstExitStatusCode == null) {
            firstExitStatusCode = statusCode
        }
        preventedSystemExitCount = preventedSystemExitCount + 1
        throw SystemExitPreventedException(statusCode)
    }

    override fun getSecurityContext(): Any? {
        return delegatedSecurityManager?.securityContext!!
    }

    override fun checkPermission(perm: Permission?) {
        delegatedSecurityManager?.checkPermission(perm)
    }

    override fun checkPermission(perm: Permission?, context: Any?) {
        delegatedSecurityManager?.checkPermission(perm, context)
    }

    override fun checkCreateClassLoader() {
        delegatedSecurityManager?.checkCreateClassLoader()
    }

    override fun checkAccess(thread: Thread) {
        delegatedSecurityManager?.checkAccess(thread)
    }

    override fun checkAccess(threadGroup: ThreadGroup) {
        delegatedSecurityManager?.checkAccess(threadGroup)
    }

    override fun checkExec(cmd: String) {
        delegatedSecurityManager?.checkExec(cmd)
    }

    override fun checkLink(lib: String) {
        delegatedSecurityManager?.checkLink(lib)
    }

    override fun checkRead(fd: FileDescriptor?) {
        delegatedSecurityManager?.checkRead(fd)
    }

    override fun checkRead(file: String) {
        delegatedSecurityManager?.checkRead(file)
    }

    override fun checkRead(file: String, context: Any) {
        delegatedSecurityManager?.checkRead(file, context)
    }

    override fun checkWrite(fd: FileDescriptor?) {
        delegatedSecurityManager?.checkWrite(fd)
    }

    override fun checkWrite(file: String) {
        delegatedSecurityManager?.checkWrite(file)
    }

    override fun checkDelete(file: String) {
        delegatedSecurityManager?.checkDelete(file)
    }

    override fun checkConnect(host: String, port: Int) {
        delegatedSecurityManager?.checkConnect(host, port)
    }

    override fun checkConnect(host: String, port: Int, context: Any) {
        delegatedSecurityManager?.checkConnect(host, port, context)
    }

    override fun checkListen(port: Int) {
        delegatedSecurityManager?.checkListen(port)
    }

    override fun checkAccept(host: String, port: Int) {
        delegatedSecurityManager?.checkAccept(host, port)
    }

    override fun checkMulticast(maddr: InetAddress?) {
        delegatedSecurityManager?.checkMulticast(maddr)
    }

    override fun checkPropertiesAccess() {
        delegatedSecurityManager?.checkPropertiesAccess()
    }

    override fun checkPropertyAccess(key: String) {
        delegatedSecurityManager?.checkPropertyAccess(key)
    }

    override fun checkPrintJobAccess() {
        delegatedSecurityManager?.checkPrintJobAccess()
    }

    override fun checkPackageAccess(pkg: String) {
        delegatedSecurityManager?.checkPackageAccess(pkg)
    }

    override fun checkPackageDefinition(pkg: String) {
        delegatedSecurityManager?.checkPackageDefinition(pkg)
    }

    override fun checkSetFactory() {
        delegatedSecurityManager?.checkSetFactory()
    }

    override fun checkSecurityAccess(target: String) {
        delegatedSecurityManager?.checkSecurityAccess(target)
    }

    override fun getThreadGroup(): ThreadGroup? {
        return delegatedSecurityManager?.threadGroup
    }
}