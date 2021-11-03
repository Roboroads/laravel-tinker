package nl.deschepers.laraveltinker.util

import com.intellij.ide.scratch.RootType
import com.intellij.ide.scratch.ScratchFileService
import com.intellij.lang.Language
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.UIBundle
import com.jetbrains.php.lang.PhpLanguage
import java.io.IOException
import javax.swing.Icon
import nl.deschepers.laraveltinker.Strings

/**
 * Most content is copied from ScratchRootType - but that class is final so I can't extend it.
 *
 * @see com.intellij.ide.scratch.ScratchRootType
 */
class LaravelTinkerConsolesRootType :
    RootType("laravel-tinker", Strings.get("lt.console.menu_title")) {

    companion object {
        fun getInstance(): LaravelTinkerConsolesRootType {
            return findByClass(LaravelTinkerConsolesRootType::class.java)
        }
    }

    override fun substituteName(project: Project, file: VirtualFile): String {
        val matches = Regex("_([0-9]+)$").find(file.nameWithoutExtension)
        val consoleNum = if (matches != null) " (${matches.groupValues[1]})" else ""
        return Strings.get("lt.console.name") + consoleNum
    }

    override fun substituteLanguage(project: Project, file: VirtualFile): Language? {
        return ScratchFileService.getInstance().scratchesMapping.getMapping(file)
    }

    override fun substituteIcon(project: Project, file: VirtualFile): Icon {
        return IconLoader.getIcon("icons/laravel-tinker-icon16.svg", javaClass)
    }

    @Suppress("SwallowedException")
    fun createScratchFile(
        project: Project?,
        text: String?,
        option: ScratchFileService.Option
    ): VirtualFile? {
        val fileName = Strings.get("lt.console.filename")
        return try {
            WriteCommandAction.writeCommandAction(project)
                .withName(Strings.get("lt.menu.action.open_new_console"))
                .withGlobalUndo()
                .shouldRecordActionForActiveDocument(false)
                .withUndoConfirmationPolicy(UndoConfirmationPolicy.REQUEST_CONFIRMATION)
                .compute<VirtualFile, IOException> {
                    val fileService = ScratchFileService.getInstance()
                    val file = fileService.findFile(this, fileName, option)
                    // save text should go before any other manipulations that load document,
                    // otherwise undo will be broken
                    VfsUtil.saveText(file, text!!)
                    fileService.scratchesMapping.setMapping(file, PhpLanguage.INSTANCE)
                    file
                }
        } catch (e: IOException) {
            Messages.showMessageDialog(
                UIBundle.message("create.new.file.could.not.create.file.error.message", fileName),
                UIBundle.message("error.dialog.title"),
                Messages.getErrorIcon()
            )
            null
        }
    }
}
